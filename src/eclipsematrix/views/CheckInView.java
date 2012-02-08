package eclipsematrix.views;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import eclipsematrix.EclipseMatrix;
import eclipsematrix.entities.MxFile;
import eclipsematrix.entities.MxFileList;
import eclipsematrix.jobs.MQLInstallJob;
import eclipsematrix.preferences.PreferenceConstants;
import eclipsematrix.utils.FileUtil;
import eclipsematrix.utils.MQLUtil;

public class CheckInView extends ViewPart {
	private SashForm top = null;
	private Text txtOutput = null;
	private Text gerLibInput = null;
	private ArrayList<String> gerLibHistory = new ArrayList<String>();
	private int count = 0;
	private int current;
	private Table jpoTable = null;
	private Table uiTable = null;
	private Table dmTable = null;
	private Action addJPOAction;
	private Action addUIAction;
	private Action addDMAction;
	private Action monitorAction;
	private TabFolder folder;

	
    private Button importBtn;
    private Composite composite1;
	
	//private FileMonitor monitor = FileMonitor.getInstance();
	//private JPOFileListener jpoFileListener = new JPOFileListener();
	private int counter = 1;
	
	private long monitorTime = System.currentTimeMillis();
	private MxFileList jpoList = new MxFileList();
	private MxFileList uiList = new MxFileList();
	private MxFileList dmList = new MxFileList();
	
	private Combo jpoCombo;
	private Combo uiCombo;
	private Combo dmCombo;
	
	public void createPartControl(Composite parent) {

		top = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		
		composite1 = new Composite(top, SWT.NONE);
		FormLayout composite1Layout = new FormLayout();
		composite1.setLayout(composite1Layout);
		{
			txtOutput = new Text(composite1, SWT.MULTI | SWT.WRAP | SWT.BORDER);
			FormData text2LData = new FormData();
			text2LData.left =  new FormAttachment(0, 1000, 5);
			text2LData.top =  new FormAttachment(0, 1000, 30);
			text2LData.right = new FormAttachment(100, 0);
			text2LData.bottom = new FormAttachment(100, -5);
			txtOutput.setLayoutData(text2LData);
		}
		{
			gerLibInput = new Text(composite1, SWT.BORDER);
			FormData text1LData = new FormData();
			text1LData.height = 18;
			text1LData.left =  new FormAttachment(0, 1000, 5);
			text1LData.top =  new FormAttachment(0, 1000, 5);
			text1LData.right = new FormAttachment(100, -85);
			gerLibInput.setLayoutData(text1LData);
			gerLibInput.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
				public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
					if (e.keyCode == 13) {
						importCalled();
					} else if (e.keyCode == 0x1000001) {
						// key up
						previousgerLibInputEntry();
					} else if (e.keyCode == 0x1000002) {
						// key down
						nextgerLibInputEntry();
					}
				}
			});
		}
		{
			importBtn = new Button(composite1, SWT.PUSH | SWT.CENTER);
			FormData button1LData = new FormData();
			button1LData.width = 80;
			button1LData.height = 20;
			button1LData.top =  new FormAttachment(0, 1000, 5);
			button1LData.right = new FormAttachment(100, 0);
			importBtn.setLayoutData(button1LData);
			importBtn.setText("Import");
			
			importBtn.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					appendText("import Button clicked\n");
					importCalled();
				}
				
				public void widgetDefaultSelected(SelectionEvent event) {
					appendText("default clicked\n");
					importCalled();
				}
			});
			
		}

		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = false;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 200;
		gridData.grabExcessVerticalSpace = true;
		
		folder = new TabFolder(top, SWT.NONE);
		{
   			Composite composite2 = createCompositeWithGrid();
			TabItem jpoTabItem = new TabItem(folder, SWT.NONE);
			jpoTabItem.setText("JPOs");
   			jpoTabItem.setControl(composite2);

   			jpoCombo = createNewViewModeCombobox(composite2, jpoCombo);
			jpoTable = createNewFileTable(composite2);
		}
    	{
    		Composite composite2 = createCompositeWithGrid();
    		TabItem uiTabItem = new TabItem(folder, SWT.NONE);
    		uiTabItem.setText("UI");
    		uiTabItem.setControl(composite2);

   			uiCombo = createNewViewModeCombobox(composite2, uiCombo);
   			uiTable = createNewFileTable(composite2);
    	}
		{
   			Composite composite2 = createCompositeWithGrid();
			TabItem dmTabItem = new TabItem(folder, SWT.NONE);
			dmTabItem.setText("DM");
   			dmTabItem.setControl(composite2);

  			dmCombo = createNewViewModeCombobox(composite2, dmCombo);
   			dmTable = createNewFileTable(composite2);
		}
		folder.setSelection(0);
	
		createActions();
		createToolbar();
		loadTableDataFromFile();
		
		IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
		if(store.getBoolean(PreferenceConstants.P_ACTIVATEMONITOR)){
			monitorFiles();
		}
		
	}


	private Composite createCompositeWithGrid() {
		Composite composite2 = new Composite(folder, SWT.NONE);
		GridLayout composite2Layout = new GridLayout();
		composite2Layout.makeColumnsEqualWidth = true;
		composite2.setLayout(composite2Layout);
		return composite2;
	}


	private Combo createNewViewModeCombobox(Composite composite2, Combo comboBox) {
		comboBox = new Combo(composite2, SWT.READ_ONLY);
		comboBox.setBounds(50, 50, 150, 65);
		String[] items = new String[] {"all", "modified"};
		comboBox.setItems(items);
		comboBox.select(1);
		comboBox.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(final SelectionEvent e) {
		    	synchroniseTableViews();
		    }
		  });
		return comboBox;
	}


	private Table createNewFileTable(Composite composite2) {
		Table fileTable = new Table(composite2, SWT.SINGLE | SWT.H_SCROLL 
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		fileTable.setHeaderVisible(true);
		fileTable.setLinesVisible(true);
		
		TableColumn column = new TableColumn(fileTable, SWT.NONE, 0);
		column.setText("Name");	
		column.setWidth(200);
		column = new TableColumn(fileTable, SWT.NONE,1);
		column.setText("Path");
		column.setWidth(250);
		column = new TableColumn(fileTable, SWT.NONE,2);
		column.setText("new");		
		column.setWidth(40);
		
		GridData table2LData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		fileTable.setLayoutData(table2LData);
		return fileTable;
	}


	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	private void loadTableDataFromFile() {
		try {
			ArrayList<MxFile> files = FileUtil.getFileData();
			for (int i = 0; i < files.size(); i++) {
				MxFile mxFile = files.get(i);
				if (mxFile.isJPOFile()) {
					jpoList.add(mxFile);
				} else if (mxFile.isUIFile()) {
					uiList.add(mxFile);
				} else if (mxFile.isDMFile()) {
					dmList.add(mxFile);
				}
			}
			synchroniseTableViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * synchroniseTableViews - synchronise Table Views with Lists
	 */
	public final void synchroniseTableViews() {
		fillTableWithData(jpoTable, jpoList, addJPOAction, jpoCombo);
		fillTableWithData(uiTable, uiList, addUIAction, uiCombo);
		fillTableWithData(dmTable, dmList, addDMAction, dmCombo);
		
		//synchronise changes with logfile
		MxFileList allLists = new MxFileList();
		allLists.addAll(jpoList);
		allLists.addAll(uiList);
		allLists.addAll(dmList);
		EclipseMatrix.getDefault().updateLogFile(allLists);
	}


	private void fillTableWithData(Table table, MxFileList fileList, Action addAction, Combo combo) {
		table.removeAll();
		addAction.setEnabled(false);
		
		Display testDisp = Display.getDefault();
		Color red = testDisp.getSystemColor(SWT.COLOR_RED);
		
		for (int i = 0; i < fileList.size(); i++) {
			MxFile mxFile = fileList.get(i);
			String changedStr = "";
			if (mxFile.getChanged()) { 
				changedStr = "*"; 
				addAction.setEnabled(true);
			}
			if (combo.getText().equals("all")) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0, mxFile.getName());
				tableItem.setText(1, mxFile.getPath());
				tableItem.setText(2, changedStr);
				if (mxFile.getError()) {
					tableItem.setBackground(red);
				}
			} else if (combo.getText().equals("modified") && mxFile.getChanged()) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0, mxFile.getName());
				tableItem.setText(1, mxFile.getPath());
				tableItem.setText(2, changedStr);
				if (mxFile.getError()) {
					tableItem.setBackground(red);
				}
			}
		}
	}
	
	
	public void createActions() {
        addJPOAction = new Action("Add JPO's") {
            public void run() { 
                       addJPOs();
               }
       };
       addUIAction = new Action("Add UI") {
           public void run() { 
                      addUI();
              }
      };
      addDMAction = new Action("Add DM") {
          public void run() { 
                     addDM();
             }
     };
       monitorAction = new Action("Monitor Files's")
       {
    	   public void run() {
    		   monitorFiles();
    	   }
       };
	}

	
    /**
     * Create toolbar.
     */
    private void createToolbar() {
            IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
            mgr.add(addJPOAction);
            mgr.add(addUIAction);
            mgr.add(addDMAction);
            mgr.add(monitorAction);
    }	
    
    private void addJPOs() {
    	MQLInstallJob job = new MQLInstallJob("JPO Install", jpoList);
    	job.setUser(true);
    	job.schedule();
    }
    
    private void addDM() {
    	dmList = dmList.sortDM();
    	MQLInstallJob job = new MQLInstallJob("Datamodel Install", dmList);
    	job.setUser(true);
    	job.schedule();
    }
    
    private void addUI() {
    	uiList = uiList.sortUI();
    	MQLInstallJob job = new MQLInstallJob("UI Install", uiList);
    	job.setUser(true);
    	job.schedule();
    }
    
    private void monitorFiles() {
    	IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
    	String jpoDir = store.getString(PreferenceConstants.P_JPOPATH);
    	String uiDir = store.getString(PreferenceConstants.P_UIPATH);
    	String dmDir = store.getString(PreferenceConstants.P_DMPATH);
		monitorAction.setEnabled(false);
//		try {
//			monitor.addWatch(new File(jpoDir));
//			monitor.addWatch(new File(uiDir));	
//			monitor.addWatch(new File(dmDir));	
//		} catch (Exception e) {
//		}
//		monitor.addFileListener(jpoFileListener);
    }
    
	public void fileChanged(File file) {
		MxFile newMxFile = new MxFile(file.getAbsolutePath(), true);
		if (newMxFile.isJPOFile()) {
			// if file is in ArrayList: look for modified < file.lastmodified
				// set changed = true 
				// synchronise table
			// if not: monitorTime < file.lastmodified 
				// set changed true 
				// add to jpo list 
				// synchronise table
			if (!jpoList.isFileInList(newMxFile)) {
				if (monitorTime < newMxFile.lastModified()) {
					jpoList.add(newMxFile);
				}
			} else {
				MxFile mxFile = jpoList.getMxFileFromList(newMxFile);
				if (mxFile.getModified() < mxFile.lastModified()) {
					mxFile.setChanged(Boolean.TRUE);
				}
			}
		} else if (newMxFile.isUIFile()) {
			if (!uiList.isFileInList(newMxFile)) {
				if (monitorTime < newMxFile.lastModified()) {
					uiList.add(newMxFile);
				}
			} else {
				MxFile mxFile = uiList.getMxFileFromList(newMxFile);
				if (mxFile.getModified() < mxFile.lastModified()) {
					mxFile.setChanged(Boolean.TRUE);
				}
			}
		} else if (newMxFile.isDMFile()) {
			if (!dmList.isFileInList(newMxFile)) {
				if (monitorTime < newMxFile.lastModified()) {
					dmList.add(newMxFile);
				}
			} else {
				MxFile mxFile = dmList.getMxFileFromList(newMxFile);
				if (mxFile.getModified() < mxFile.lastModified()) {
					mxFile.setChanged(Boolean.TRUE);
				}
			}		
		}
		synchroniseTableViews();
	}
	
	/**
	 * Add new or existing File to table from other views or actions.
	 * @param newMxFile
	 */
	public void addMxFileToTable(MxFile newMxFile){
		if (newMxFile.isJPOFile() && !jpoList.isFileInList(newMxFile)) {
			jpoList.add(newMxFile);
		} else if (newMxFile.isUIFile() && !uiList.isFileInList(newMxFile)) {
			uiList.add(newMxFile);
		} else if (newMxFile.isDMFile() && !dmList.isFileInList(newMxFile)) {
			dmList.add(newMxFile);
		}
		synchroniseTableViews();
	}

	public void fileDeleted(File file) {
		MxFile newMxFile = new MxFile(file.getAbsolutePath(), true);
		if (newMxFile.isJPOFile() && jpoList.isFileInList(newMxFile)) {
			jpoList.removeFileFromList(newMxFile);
		} else if (newMxFile.isUIFile() && uiList.isFileInList(newMxFile)) {
			uiList.removeFileFromList(newMxFile);
		} else if (newMxFile.isDMFile() && dmList.isFileInList(newMxFile)) {
			dmList.removeFileFromList(newMxFile);
		}
		synchroniseTableViews();
	}
	
	public void fileCreated(File file){
		MxFile newMxFile = new MxFile(file.getAbsolutePath(), true);
		if (newMxFile.isJPOFile()) {
			jpoList.add(newMxFile);
		} else if (newMxFile.isUIFile()) {
			uiList.add(newMxFile);
		} else if (newMxFile.isDMFile()) {
			dmList.add(newMxFile);
		}
		synchroniseTableViews();
	}
		
    private void importCalled(){
    	MQLUtil mql = new MQLUtil();
    	try {
    		count++; 
    		current = count;
    		String text = gerLibInput.getText();
    		gerLibHistory.add(text);
    		gerLibInput.setText("");
    		String result = mql.genericImport(text);
    		appendText("Successful added: " + text + "\n" + result + "\n");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			appendText(e.getMessage());
			e.printStackTrace();
		}
    }
    
    
    public void appendText(String text) {
    	txtOutput.append("<Log " + counter + "> " + text + "\n");
    	counter++;
    }
    
    
    private void previousgerLibInputEntry() {
		if(current > 0) {
			current--;
			gerLibInput.setText((String) gerLibHistory.get(current));
			gerLibInput.setSelection(((String) gerLibHistory.get(current)).length());
		}
    }
    
    private void nextgerLibInputEntry() {
		if (current < count-1) {
			current++;
			gerLibInput.setText((String) gerLibHistory.get(current));
			gerLibInput.setSelection(((String) gerLibHistory.get(current)).length());
		}
    	
    }
}
