package eclipsematrix.checkin.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import eclipsematrix.entities.RecordProvider;
import eclipsematrix.jobs.NewMQLInstallJob;

public class JpoCheckin extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewMQLInstallJob job = new NewMQLInstallJob("JPO Install",
				RecordProvider.INSTANCE.getJPORecords());
		job.setUser(true);
		job.schedule();
		return null;
	}

}
