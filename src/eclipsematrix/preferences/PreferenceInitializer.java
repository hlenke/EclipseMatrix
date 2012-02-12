package eclipsematrix.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eclipsematrix.EclipseMatrix;

/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_ACTIVATEMONITOR, false);
		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_HOST,
				"localhost");
		
		store.setDefault(PreferenceConstants.P_USERNAME, "creator");
		store.setDefault(PreferenceConstants.P_PASSWORD, "");
	}

}
