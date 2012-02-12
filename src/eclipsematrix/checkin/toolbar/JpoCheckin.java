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
public class JpoCheckin extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MQLInstallJob job = new MQLInstallJob("JPO Install",
				RecordProvider.INSTANCE.getJPORecords());
		job.setUser(true);
		job.schedule();
		return null;
	}

}
