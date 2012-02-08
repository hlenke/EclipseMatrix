package eclipsematrix.utils;

import net.contentobjects.jnotify.JNotifyListener;

import org.eclipse.ui.PlatformUI;

import eclipsematrix.entities.RecordProvider;
import eclipsematrix.entities.ConfigFileRecord;

public class FileListener implements JNotifyListener {
	
	public void fileRenamed(int wd, String rootPath, String oldName,
	        String newName) {
    	if (ConfigFileUtil.isValidConfigFile(newName)) {
  	      final String fName = newName;
	      final String fPath = rootPath;
	      final String fOldName = oldName;
	      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ConfigFileRecord oldRecord = new ConfigFileRecord(fOldName, fPath, false);  
					ConfigFileRecord record = new ConfigFileRecord(fName, fPath, true);
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
				}});
    	}
	    }
    
    
	    public void fileModified(int wd, String rootPath, String name) {
	    	if (ConfigFileUtil.isValidConfigFile(name)) {
			      final String fName = name;
			      final String fPath = rootPath;
			      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							ConfigFileRecord record = new ConfigFileRecord(fName, fPath, true);
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
						}});
	    	}
	    }
	    
	    
	    public void fileDeleted(int wd, String rootPath, String name) {
	    	if (ConfigFileUtil.isValidConfigFile(name)) {
		    	System.err.println("file deleted root path " + rootPath + " name " + name );
			      final String fName = name;
			      final String fPath = rootPath;
			      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							ConfigFileRecord record = new ConfigFileRecord(fName, fPath, true);
							  if (ConfigFileUtil.isJPOFile(fName)) {
								  RecordProvider.INSTANCE.deleteJPORecord(record);
							  } else if (ConfigFileUtil.isDMFile(fName)) {
								  RecordProvider.INSTANCE.deleteDMRecord(record);
							  } else if (ConfigFileUtil.isUIFile(fName)) {
								  RecordProvider.INSTANCE.deleteUIRecord(record);
							  }
						}});
	    	}
	    }
	    
	    
	    public void fileCreated(int wd, String rootPath, String name) {
	    	if (ConfigFileUtil.isValidConfigFile(name)) {
			      final String fName = name;
			      final String fPath = rootPath;
			      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							ConfigFileRecord record = new ConfigFileRecord(fName, fPath, true);
							  if (ConfigFileUtil.isJPOFile(fName)) {
								  RecordProvider.INSTANCE.addJPORecord(record);
							  } else if (ConfigFileUtil.isDMFile(fName)) {
								  RecordProvider.INSTANCE.addDMRecord(record);
							  } else if (ConfigFileUtil.isUIFile(fName)) {
								  RecordProvider.INSTANCE.addUIRecord(record);
							  }
						}});
	    	}
	    }
	    	    
	    void print(String msg) {
	      System.err.println(msg);
	    }
}
