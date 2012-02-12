package eclipsematrix.checkin.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import eclipsematrix.entities.RecordProvider;
import eclipsematrix.jobs.MQLInstallJob;

/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 * 
 */
public class DMCheckin extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MQLInstallJob job = new MQLInstallJob("DM Install",
				RecordProvider.INSTANCE.getDMRecords());
		job.setUser(true);
		job.schedule();
		return null;
	}

}
