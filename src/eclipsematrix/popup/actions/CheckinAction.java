package eclipsematrix.popup.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import matrix.db.Context;
import matrix.db.MQLCommand;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import eclipsematrix.EclipseMatrix;
import eclipsematrix.entities.MxFile;
import eclipsematrix.utils.FileUtil;
import eclipsematrix.utils.MQLUtil;

public class CheckinAction implements IObjectActionDelegate {

	private String mqlReturn;
	private List filePath = new ArrayList();
	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public CheckinAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		//clear ReturnString
		mqlReturn = "";
		try {

			MQLUtil mql = new MQLUtil();
			for (Iterator iter = filePath.iterator(); iter.hasNext();) {
				String file = (String) iter.next();
				mqlReturn += mql.genericImport(file);
				MxFile newMxFile = new MxFile(file, false);
				EclipseMatrix.getDefault().getCheckInView().addMxFileToTable(newMxFile);
			}
			//kill checkdin entrys
			filePath.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		MessageDialog.openInformation(
//			shell,
//			"EclipseMatrix Plug-in",
//			"JPOCheckin was executed.");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
 	 		 IStructuredSelection structuredSelection = (IStructuredSelection) selection;
 	 		 	//kill old entrys
 	 		 	filePath.clear();
 	  		    for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
					IFile element = (IFile) iter.next();
					filePath.add(element.getLocation().toString());
					
				} 		    
 	  		}		
	}

}
