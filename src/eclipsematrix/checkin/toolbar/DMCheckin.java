package eclipsematrix.checkin.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class DMCheckin extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
//    	MQLInstallJob job = new MQLInstallJob("JPO Install", jpoList);
//    	job.setUser(true);
//    	job.schedule();
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(
					event).getShell(), "Info", "Info for you");
			return null;
	}

}
