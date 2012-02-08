package eclipsematrix.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ConfigFileRecord {

	private String name;
	private String path;
	private boolean changed;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	
	public ConfigFileRecord(String name, String path, boolean changed) {
		super();
		this.name = name;
		this.path = path;
		this.changed = changed;
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		propertyChangeSupport.firePropertyChange("name", this.name,
				this.name = name);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		propertyChangeSupport.firePropertyChange("path", this.path, this.path = path);
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		propertyChangeSupport.firePropertyChange("changed", this.changed, this.changed = changed);
	}

	@Override
	public boolean equals(Object obj) {
		//FIXME implement code from effective JAVA
		//TODO override hash function???
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof ConfigFileRecord) {
			ConfigFileRecord record = (ConfigFileRecord) obj;
			if (path.equals(record.getPath()) && name.equals(record.getName())) {
				return true;
			}
		}
		return false;
	}

	
	
	
}
