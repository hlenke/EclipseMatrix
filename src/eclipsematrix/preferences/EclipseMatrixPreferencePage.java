package eclipsematrix.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eclipsematrix.EclipseMatrix;


public class EclipseMatrixPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public EclipseMatrixPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipseMatrix.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}
	
	@Override
	public void createFieldEditors() {
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_ACTIVATEMONITOR,
				"&Activate monitoring at Startup.",
				getFieldEditorParent()));

		addField(new DirectoryFieldEditor(PreferenceConstants.P_LOGFILEPATH, 
				"&Logifle - Path:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_JPOPATH, 
				"&JPO - Directory:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_UIPATH,
				"&UI - Directory:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_DMPATH,
				"&DataModel - Directory:", getFieldEditorParent()));
		addField(
			new StringFieldEditor(PreferenceConstants.P_HOST, "Host:", getFieldEditorParent()));
		addField(
				new 
				StringFieldEditor(PreferenceConstants.P_USERNAME, "Username:", getFieldEditorParent()));
		addField(
				new 
				StringFieldEditor(PreferenceConstants.P_PASSWORD, "Password:", getFieldEditorParent()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}