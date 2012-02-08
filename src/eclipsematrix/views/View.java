package eclipsematrix.views;

import java.io.WriteAbortedException;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import at.bestsolution.dataforms.util.viewers.GenericObservableMapCellLabelProvider;
import eclipsematrix.entities.RecordProvider;
import eclipsematrix.entities.ConfigFileRecord;

//RIGHTCLICK delete from list

public class View extends ViewPart {
	public static final String ID = "eclipsematrix.views.View";

	private TableViewer jpoViewer;
	private TableViewer uiViewer;
	private TableViewer dmViewer;
	private Combo jpoCombo;
	private Combo dmCombo;
	private Combo uiCombo;
	private WritableList input;
	
	
	public void createPartControl(Composite parent) {
//		GridLayout layout = new GridLayout(2, false);
//		parent.setLayout(layout);
		
		parent.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		{
			Composite tableComposite = createCompositeWithGrid(folder);
			TabItem jpoTabItem = new TabItem(folder, SWT.NONE);
			jpoTabItem.setText("JPO");
			jpoTabItem.setControl(tableComposite);
			jpoCombo = createNewViewModeCombobox(tableComposite);
			jpoViewer = createViewer(tableComposite, jpoCombo, RecordProvider.INSTANCE.getJPORecords());
			jpoCombo.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(final SelectionEvent e) {
			    	jpoViewer.refresh();
			    }
			});
		}
		{
			Composite tableComposite = createCompositeWithGrid(folder);
			TabItem jpoTabItem = new TabItem(folder, SWT.NONE);
			jpoTabItem.setText("UI");
			jpoTabItem.setControl(tableComposite);
			uiCombo = createNewViewModeCombobox(tableComposite);
			uiViewer = createViewer(tableComposite, uiCombo, RecordProvider.INSTANCE.getUIRecords());
			uiCombo.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(final SelectionEvent e) {
			    	uiViewer.refresh();
			    }
			});
		}
		{
			Composite tableComposite = createCompositeWithGrid(folder);
			TabItem jpoTabItem = new TabItem(folder, SWT.NONE);
			jpoTabItem.setText("DM");
			jpoTabItem.setControl(tableComposite);
			dmCombo = createNewViewModeCombobox(tableComposite);
			dmViewer = createViewer(tableComposite, dmCombo, RecordProvider.INSTANCE.getDMRecords());
			dmCombo.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(final SelectionEvent e) {
			    	dmViewer.refresh();
			    }
			});
		}
	}
	
	private Composite createCompositeWithGrid(Composite folder) {
		Composite composite2 = new Composite(folder, SWT.NONE);
		GridLayout composite2Layout = new GridLayout();
		composite2Layout.makeColumnsEqualWidth = true;
		composite2.setLayout(composite2Layout);
		return composite2;
	}
	
	private Combo createNewViewModeCombobox(Composite composite2) {
		Combo comboBox = new Combo(composite2, SWT.READ_ONLY);
		comboBox.setBounds(50, 50, 150, 65);
		String[] items = new String[] {"all", "modified"};
		comboBox.setItems(items);
		comboBox.select(1);
		return comboBox;
	}


	private TableViewer createViewer(Composite parent,final Combo combo, WritableList input) {
		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		viewer.setContentProvider(contentProvider);
		
		createColumns(parent, viewer, contentProvider);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		//FIXME remove
//		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		
		
//		List<TableConfigFileRecord> records = RecordProvider.INSTANCE.getRecords();
//		input = new WritableList(records, TableConfigFileRecord.class);
		
		// Set the writeableList as input for the viewer
		viewer.setInput(input);
		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (combo.getText().equals("all")){
					return true;
				}
				return ((ConfigFileRecord) element).isChanged();
			}
		});
		
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		return viewer;
	}


	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer, final ObservableListContentProvider contentProvider) {
		String[] titles = { "name", "path", "changed"};
		int[] bounds = { 100, 100, 100 };

		IObservableSet knownElements = contentProvider.getKnownElements();
		final IObservableMap names = BeanProperties.value(ConfigFileRecord.class,
				"name").observeDetail(knownElements);
		final IObservableMap paths = BeanProperties.value(ConfigFileRecord.class,
				"path").observeDetail(knownElements);
		final IObservableMap changed = BeanProperties.value(ConfigFileRecord.class,
		"changed").observeDetail(knownElements);

		IObservableMap[] labelMaps = { names, paths, changed };

		// First column is for the first name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0, viewer);
		col.setLabelProvider(new GenericObservableMapCellLabelProvider(labelMaps, "{0}"));

		// Second column is for the last name
		col = createTableViewerColumn(titles[1], bounds[1], 1, viewer);
		col.setLabelProvider(new GenericObservableMapCellLabelProvider(labelMaps, "{1}"));

		// Now the gender
		col = createTableViewerColumn(titles[2], bounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ConfigFileRecord p = (ConfigFileRecord) element;
				return String.valueOf(p.isChanged());
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber, TableViewer viewer) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	
/** * Passing the focus request to the viewer's control. */

	public void setFocus() {
		jpoViewer.getControl().setFocus();
	}
}