package eclipsematrix.checkin.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import eclipsematrix.entities.RecordProvider;
import eclipsematrix.jobs.NewMQLInstallJob;

public class UICheckin extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewMQLInstallJob job = new NewMQLInstallJob("JPO Install",
				RecordProvider.INSTANCE.getUIRecords());
		job.setUser(true);
		job.schedule();
		return null;
	}

}
