package eclipsematrix.entities;

import org.eclipse.core.databinding.observable.list.WritableList;


public enum RecordProvider {
	INSTANCE;
	
	private WritableList uiRecords = new WritableList();
	private WritableList dmRecords = new WritableList();
	private WritableList jpoRecords = new WritableList();
	
	private RecordProvider() {
		jpoRecords.add(new ConfigFileRecord("name", "path", true));
		jpoRecords.add(new ConfigFileRecord("name", "path", false));
		
		uiRecords.add(new ConfigFileRecord("name", "path", false));
		dmRecords.add(new ConfigFileRecord("name", "path", false));
	}
	
	public WritableList getJPORecords() {
		return jpoRecords;
	}
	
	public void addJPORecord(ConfigFileRecord record) {
		jpoRecords.add(record);
	}
	
	public void deleteJPORecord(ConfigFileRecord record) {
		if (jpoRecords.contains(record)) {
			jpoRecords.remove(record);
		}
	}
	
	public WritableList getDMRecords() {
		return dmRecords;
	}
	
	public void addDMRecord(ConfigFileRecord record) {
		dmRecords.add(record);
	}
	
	public void deleteDMRecord(ConfigFileRecord record) {
		if (dmRecords.contains(record)) {
			dmRecords.remove(record);
		}
	}
	
	public WritableList getUIRecords() {
		return uiRecords;
	}
	
	public void addUIRecord(ConfigFileRecord record) {
		uiRecords.add(record);
	}
	
	public void deleteUIRecord(ConfigFileRecord record) {
		if (uiRecords.contains(record)) {
			uiRecords.remove(record);
		}
	}
	
}
