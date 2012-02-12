package eclipsematrix.popup.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import eclipsematrix.entities.ConfigFileRecord;
import eclipsematrix.entities.RecordProvider;
import eclipsematrix.utils.MQLUtil;

/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 * 
 */
public class CheckinAction implements IObjectActionDelegate {

	private List<String> filePaths = new LinkedList<String>();

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
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		// clear ReturnString
		try {

			MQLUtil mql = new MQLUtil();
			for (String file : filePaths) {
				mql.jpoImport(file);
				RecordProvider.INSTANCE.addRecord(new ConfigFileRecord(file,
						false));
			}
			filePaths.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			// kill old entrys
			filePaths.clear();
			for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
				IFile element = (IFile) iter.next();
				filePaths.add(element.getLocation().toString());

			}
		}
	}

}
