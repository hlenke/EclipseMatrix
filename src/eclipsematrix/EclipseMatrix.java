package eclipsematrix;

import java.util.ArrayList;

import matrix.db.Context;
import matrix.util.MatrixException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import eclipsematrix.entities.MxFile;
import eclipsematrix.entities.MxFileList;
import eclipsematrix.matrix.MatrixOperations;
import eclipsematrix.preferences.PreferenceConstants;
import eclipsematrix.utils.FileUtil;
import eclipsematrix.views.CheckInView;

/**
 * 
 * @author Administrator
 * 
 *         TODO : 3. Vision zwischenablage liste schon vorhanden:
 *         http://sourceforge.net/forum/forum.php?forum_id=916555 
 *         
 *         
 *         5. bei process start button disablen danach wieder
 *         enablen 
 *         Number Generator Object Creation Setups berücksichtigen 
 *         Menu vor Menu Actions importieren geht nicht --> Idee ? 
 *         - matrixexception rausnehmen - versuchen
 *         das logging zu verbessern --> näher an die mql ausgaben ran
 *         (workaround bei import über textfeld dateinamen mit anzeigen) DONE -
 *         export - unsufficent Memory - Repository wechseln altes Repo angeben
 *         neues Repo angeben JPOs und Commands löschen --> neue einspielen! -
 *         Plugin: Mass Checkin mit Checkbox Popup Dialog
 * 
 *         DONE : - Log immer new line DONE - Löschen des inputfields nach
 *         checkin DONE - hoch runter --> alte befehle zeigen oder die letzten 5
 *         in einem Context Menu DONE - cmd ui und jpo files die nur vom
 *         explorer getoucht wurden --> Datumsvergleich von geändert nach dem
 *         "Monitor" DONE - Fortschrittsbalken für App DONE - Rename reagieren
 *         --> old aus liste löschen neues hinzufügen 20.02.09 2. EclipseMatrix
 *         FileList muss sich aus den einzelnen MXLists ergeben und nicht extra
 *         gepflegt werden
 *         4. implement cancel for import process 
 *         5. Filtern der Tabellen (changed, all..) 
 *         support für Attribute verbessern STRING REAL INTEGER etc
 */
public class EclipseMatrix extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "EclipseMatrix";

	// The shared instance
	private static EclipseMatrix plugin;

	private Context ctx;
	private String host;
	private String user;
	private String password;
	private ArrayList<MxFile> mxFileList = null;

	/**
	 * The constructor.
	 */
	public EclipseMatrix() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EclipseMatrix getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * login to matrix.
	 * @param store Preferences Store from Eclipse
	 * @throws MatrixException Exception from MX.
	 */
	public final void loginDirect(final IPreferenceStore store) throws MatrixException {
		MatrixOperations mxops = new MatrixOperations();
		mxops.setHost(store.getString(PreferenceConstants.P_HOST));
		mxops.setUser(store.getString(PreferenceConstants.P_USERNAME));
		mxops.setPassword(store.getString(PreferenceConstants.P_PASSWORD));
		mxops.login();
		this.host = store.getString(PreferenceConstants.P_HOST);
		this.user = store.getString(PreferenceConstants.P_USERNAME);
		this.password = store.getString(PreferenceConstants.P_PASSWORD);
		// MxEclipseObjectView.refreshViewStatusBar(null);
	}

	/**
	 * @return Returns the context.
	 */
	public Context getContext() {
		if (this.ctx == null) {
			System.out.println("getContext:isNull");
			IPreferenceStore store = EclipseMatrix.getDefault()
					.getPreferenceStore();
			boolean silentLogin = true;// store.getBoolean(PreferenceConstants.MATRIX_AUTOMATIC_SILENT_LOGIN);
			if (silentLogin) {
				try {
					this.loginDirect(store);
				} catch (MatrixException e) {
					System.out.println(e.getMessage());
					// MxEclipseLogger.getLogger().severe("Error when tried to login "
					// + e.getMessage());
				}
			}
		}
		return this.ctx;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param context
	 *            The context to set.
	 */
	public void setContext(Context context) {
		this.ctx = context;
	}

	public CheckInView getCheckInView() {
		return (CheckInView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(
						"eclipsematrix.views.CheckInView");
	}

//	public final ArrayList<MxFile> getMxFileList() {
//		if (this.mxFileList == null) {
//			try {
//				this.mxFileList = FileUtil.getFileData();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//		return this.mxFileList;
//	}

	/**
	 * generate Logfile from List of MxFiles.
	 */
	public void updateLogFile(final MxFileList list) {
		FileUtil.generateTextFile(list);
	}
}
