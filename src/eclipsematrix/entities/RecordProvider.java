package eclipsematrix.entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.WritableList;

import eclipsematrix.utils.ConfigFileUtil;
import eclipsematrix.utils.StateLoader;


public enum RecordProvider implements IListChangeListener, PropertyChangeListener {
	INSTANCE;
	
	
	private StateLoader stateLoader = new StateLoader();
	private WritableList uiRecords = new WritableList();
	private WritableList dmRecords = new WritableList();
	private WritableList jpoRecords = new WritableList();
	
	private RecordProvider() {
		try {
			List<ConfigFileRecord> saved = stateLoader.loadState();
			for (ConfigFileRecord configFileRecord : saved) {
				if (ConfigFileUtil.isJPOFile(configFileRecord.getName())) {
					addJPORecord(configFileRecord);
				} else if (ConfigFileUtil.isDMFile(configFileRecord.getName())) {
					addDMRecord(configFileRecord);
				} else  {
					addUIRecord(configFileRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jpoRecords.addListChangeListener(this);
		uiRecords.addListChangeListener(this);
		dmRecords.addListChangeListener(this);
		
	}
	
	public WritableList getJPORecords() {
		return jpoRecords;
	}
	
	public void addJPORecord(ConfigFileRecord record) {
		record.addPropertyChangeListener("name", this);
		record.addPropertyChangeListener("changed", this);
		jpoRecords.add(record);
	}
	
	public void deleteJPORecord(ConfigFileRecord record) {
		record.removePropertyChangeListener(this);
		if (jpoRecords.contains(record)) {
			jpoRecords.remove(record);
		}
	}
	
	public WritableList getDMRecords() {
		return dmRecords;
	}
	
	public void addDMRecord(ConfigFileRecord record) {
		record.addPropertyChangeListener("name", this);
		record.addPropertyChangeListener("changed", this);
		dmRecords.add(record);
	}

	public void addRecord(ConfigFileRecord record) {
		if (ConfigFileUtil.isJPOFile(record.getName())) {
			addJPORecord(record);
		} else if (ConfigFileUtil.isUIFile(record.getName())) {
			addUIRecord(record);
		}
		dmRecords.add(record);
	}
	
	public void deleteDMRecord(ConfigFileRecord record) {
		record.removePropertyChangeListener(this);
		if (dmRecords.contains(record)) {
			dmRecords.remove(record);
		}
	}
	
	public WritableList getUIRecords() {
		return uiRecords;
	}
	
	public void addUIRecord(ConfigFileRecord record) {
		record.addPropertyChangeListener("name", this);
		record.addPropertyChangeListener("changed", this);
		uiRecords.add(record);
	}
	
	public void deleteUIRecord(ConfigFileRecord record) {
		record.removePropertyChangeListener(this);
		if (uiRecords.contains(record)) {
			uiRecords.remove(record);
		}
	}

	@SuppressWarnings("unchecked")
	public void handleListChange(ListChangeEvent event) {
		System.err.println("list changed");
		List<ConfigFileRecord> all = new LinkedList<ConfigFileRecord>();
		all.addAll(uiRecords);
		all.addAll(dmRecords);
		all.addAll(jpoRecords);
		stateLoader.generateTextFile(all);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		System.err.println("property changed");
		List<ConfigFileRecord> all = new LinkedList<ConfigFileRecord>();
		all.addAll(uiRecords);
		all.addAll(dmRecords);
		all.addAll(jpoRecords);
		stateLoader.generateTextFile(all);
	}
	
}
