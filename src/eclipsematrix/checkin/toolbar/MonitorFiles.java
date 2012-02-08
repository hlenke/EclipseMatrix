package eclipsematrix.checkin.toolbar;

import net.contentobjects.jnotify.JNotify;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

import eclipsematrix.EclipseMatrix;
import eclipsematrix.preferences.PreferenceConstants;
import eclipsematrix.utils.FileListener;

public class MonitorFiles extends AbstractHandler {
	//private FileMonitor monitor = FileMonitor.getInstance();
	//private JPOFileListener jpoFileListener = new JPOFileListener();
	
	boolean enabled = true;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
		//FIXME check prefs
    	String jpoDir = store.getString(PreferenceConstants.P_JPOPATH);
    	String uiDir = store.getString(PreferenceConstants.P_UIPATH);
    	String dmDir = store.getString(PreferenceConstants.P_DMPATH);
		
		try {
			setFilemonitor(jpoDir);
			setFilemonitor(uiDir);
			setFilemonitor(dmDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(
//		event).getShell(), "Info", "Files Monitored");
		
		//FIXME force refresh of command
		enabled = false;
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		boolean isEnabled = super.isEnabled();
		if (!isEnabled) {
			return false;
		}
		return enabled;
	}




	public void setFilemonitor(String path) throws Exception {
		    // watch mask, specify events you care about,
		    // or JNotify.FILE_ANY for all events.
		    int mask = JNotify.FILE_CREATED  | 
		               JNotify.FILE_DELETED  | 
		               JNotify.FILE_MODIFIED | 
		               JNotify.FILE_RENAMED;

		    // watch subtree?
		    boolean watchSubtree = true;

		    // add actual watch
		    int watchID = JNotify.addWatch(path, mask, watchSubtree, new FileListener());
//		    // to remove watch the watch
//		    boolean res = JNotify.removeWatch(watchID);
//		    if (!res) {
//		      // invalid watch ID specified.
//		    }
		  }
}
