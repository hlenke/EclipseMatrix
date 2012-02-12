package eclipsematrix.utils;

import java.io.File;

import net.contentobjects.jnotify.JNotifyListener;

import org.eclipse.ui.PlatformUI;

import eclipsematrix.entities.RecordProvider;
import eclipsematrix.entities.ConfigFileRecord;

public class FileListener implements JNotifyListener {

	public void fileRenamed(int wd, String rootPath, String oldName,
			String newName) {
		if (ConfigFileUtil.isValidConfigFile(newName)) {
			final String fName = getName(newName);
			final String fPath = getAbsolutePath(rootPath, newName);
			final String fOldName = getName(oldName);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ConfigFileRecord oldRecord = new ConfigFileRecord(fOldName,
							fPath, false);
					ConfigFileRecord record = new ConfigFileRecord(fName,
							fPath, true);
					if (ConfigFileUtil.isJPOFile(fName)) {
						RecordProvider.INSTANCE.deleteJPORecord(oldRecord);
						RecordProvider.INSTANCE.addJPORecord(record);
					} else if (ConfigFileUtil.isDMFile(fName)) {
						RecordProvider.INSTANCE.deleteDMRecord(oldRecord);
						RecordProvider.INSTANCE.addDMRecord(record);
					} else if (ConfigFileUtil.isUIFile(fName)) {
						RecordProvider.INSTANCE.deleteUIRecord(oldRecord);
						RecordProvider.INSTANCE.addUIRecord(record);
					}
				}
			});
		}
	}

	public void fileModified(int wd, String rootPath, String name) {
		if (ConfigFileUtil.isValidConfigFile(name)) {
			final String fName = getName(name);
			final String fPath = getAbsolutePath(rootPath, name);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ConfigFileRecord record = new ConfigFileRecord(fName,
							fPath, true);
					if (ConfigFileUtil.isJPOFile(fName)) {
						RecordProvider.INSTANCE.deleteJPORecord(record);
						RecordProvider.INSTANCE.addJPORecord(record);
					} else if (ConfigFileUtil.isDMFile(fName)) {
						RecordProvider.INSTANCE.deleteDMRecord(record);
						RecordProvider.INSTANCE.addDMRecord(record);
					} else if (ConfigFileUtil.isUIFile(fName)) {
						RecordProvider.INSTANCE.deleteUIRecord(record);
						RecordProvider.INSTANCE.addUIRecord(record);
					}
				}
			});
		}
	}

	public void fileDeleted(int wd, String rootPath, String name) {
		if (ConfigFileUtil.isValidConfigFile(name)) {
			final String fName = getName(name);
			final String fPath = getAbsolutePath(rootPath, name);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ConfigFileRecord record = new ConfigFileRecord(fName,
							fPath, true);
					if (ConfigFileUtil.isJPOFile(fName)) {
						RecordProvider.INSTANCE.deleteJPORecord(record);
					} else if (ConfigFileUtil.isDMFile(fName)) {
						RecordProvider.INSTANCE.deleteDMRecord(record);
					} else if (ConfigFileUtil.isUIFile(fName)) {
						RecordProvider.INSTANCE.deleteUIRecord(record);
					}
				}
			});
		}
	}

	public void fileCreated(int wd, String rootPath, String name) {
		if (ConfigFileUtil.isValidConfigFile(name)) {
			final String fName = getName(name);
			final String fPath = getAbsolutePath(rootPath, name);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ConfigFileRecord record = new ConfigFileRecord(fName,
							fPath, true);
					if (ConfigFileUtil.isJPOFile(fName)) {
						RecordProvider.INSTANCE.addJPORecord(record);
					} else if (ConfigFileUtil.isDMFile(fName)) {
						RecordProvider.INSTANCE.addDMRecord(record);
					} else if (ConfigFileUtil.isUIFile(fName)) {
						RecordProvider.INSTANCE.addUIRecord(record);
					}
				}
			});
		}
	}

	private String getAbsolutePath(String rootPath, String name) {
		return rootPath + File.separator + name;
	}

	private String getName(String name) {
		String[] parts = name.split(File.separator + File.separator);
		return parts[parts.length - 1];
	}

}
