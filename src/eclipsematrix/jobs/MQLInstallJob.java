package eclipsematrix.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;

import eclipsematrix.entities.MxFile;
import eclipsematrix.entities.MxFileList;
import eclipsematrix.utils.MQLUtil;
import eclipsematrix.views.CheckInView;

/**
 * Progressmonitor Job for Installs with emxGerLib.
 * @author Administrator
 *
 */
public class MQLInstallJob extends Job {

	/**
	 * holds the FileList.
	 */
	private MxFileList mxList;
	
	/**
	 * holds the CheckinView.
	 */
	private CheckInView view = (CheckInView) PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage().findView(
					"eclipsematrix.views.CheckInView");

	/**
	 * Constructor.
	 * @param name Name of the Task.
	 * @param mxList get The List.
	 */
	public MQLInstallJob(final String name, final MxFileList mxList) {
		super(name);
		this.mxList = mxList;
	}

	@Override
	protected final IStatus run(final IProgressMonitor monitor) {
		monitor.beginTask("Install file(s)", 100);
		monitor.subTask("get context");
		MQLUtil mql = new MQLUtil();
		monitor.worked(20);
		// count files
		int y = 0;
		for (int i = 0; i < mxList.size(); i++) {
			MxFile mxFile = mxList.get(i);
			if (mxFile.getChanged()) {
				y++;
			}
		}
		monitor.worked(10);
		if (y > 0) {
			// calculate subTaskAmount based on file count
			int subTaskAmount = 70 / y;

			for (int i = 0; i < mxList.size(); i++) {
				if (monitor.isCanceled()) {
					syncView();
					return Status.CANCEL_STATUS;
				}

				MxFile mxFile = mxList.get(i);
				String resultStr = new String();
				if (mxFile.getChanged()) {
					try {
						resultStr = mql.genericImport(mxFile.getAbsolutePath());
						setLogMsg("Successful added: "
								+ mxFile.getAbsolutePath() + "\n" + resultStr
								+ "\n");
						mxFile.setChanged(Boolean.FALSE);
						mxFile.setError(Boolean.FALSE);
						monitor.worked(subTaskAmount);
					} catch (Exception e) {
						e.printStackTrace();
						mxFile.setError(Boolean.TRUE);
						setLogMsg("ERROR " + e.getMessage());
					}
				}
			}
		}
		monitor.done();
		syncView();
		return Status.OK_STATUS;
	}

	/**
	 * Sends Text to Checkinview LogWindow.
	 * 
	 * @param text
	 *            new Text
	 */
	private void setLogMsg(final String text) {
		final String atext = text;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (view != null) {
					view.appendText(atext);
				}
			}
		});
	}

	/**
	 * Syncs Tables in Checkinview with {@link MxFileList} changes.
	 */
	private void syncView() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (view != null) {
					view.synchroniseTableViews();
				}
			}
		});
	}

}
