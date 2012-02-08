package eclipsematrix.entities;

import java.io.File;

public class MxFile extends File {
	
	static final long serialVersionUID = -1L;
	
	private Boolean changed;
	private Boolean error = Boolean.FALSE;
	private CategoryEnum category;
	private long modified;
	private Boolean visible;
	
	public enum CategoryEnum {
		JPO, CHANNEL, COMMAND, FORM, MENU, PORTAL, TABLE, ATTRIBUTE, GROUP, POLICY, RELATIONSHIP, ROLE, TYPE
	}
	
	public enum AttributeCategoryEnum {
		STRING, BOOLEAN, INTEGER, REAL, DATE
	}
	
	public enum UICategory {
		CHANNEL, COMMAND, FORM, MENU, PORTAL, TABLE
	}
	
	
	public MxFile(String absolutePath, Boolean changed, CategoryEnum cat){
		super(absolutePath);
		this.changed = changed;
		this.category =  cat;
		//FIXME do we need this?
		setModified(this.lastModified());
	}
	
	public MxFile(String absolutePath, Boolean changed){
		super(absolutePath);
		this.changed = changed;
		this.category = getCatHelper();
		setModified(this.lastModified());
	}
	
	public Boolean getChanged() {
		return changed;
	}
	public void setChanged(Boolean changed) {
		this.changed = changed;
	}
	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public CategoryEnum getCategory(){
		return category;
	}
	
	public void setCategory(CategoryEnum category){
		this.category = category;
	}
	
	public String toFileString(){
		String changedStr = "-";
		if (this.getChanged()){
			changedStr = "*";
		}
		return this.getAbsolutePath() + "#" + changedStr + "#" + category.toString();
	}

	private CategoryEnum getCatHelper() {
		String name = this.getName();
		String fileExtension = name.split("\\.")[1];
		//no pur java or tcl file
		if (!fileExtension.equals("java") && !fileExtension.equals("tcl")){
			return null;
		}
		System.err.println("fileExtension " + fileExtension);
		for (MxFile.CategoryEnum category : MxFile.CategoryEnum.values()) {
			if (name.contains(category.toString() + "_") || (name.contains("mx" + category.toString()))){
				System.err.println("name " + name);
				return category;
			}
		}
		//workaround for Attributes
		for (MxFile.AttributeCategoryEnum attribCat : MxFile.AttributeCategoryEnum.values()){
			if (name.contains(attribCat.toString() + "_")){
				return MxFile.CategoryEnum.ATTRIBUTE;
			}
		}
		return null;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	public boolean isUIFile(){
		UICategory[] cats = UICategory.values();
		for (UICategory uiCategory : cats) {
			//FIXME this should be on category
			if (getCategory().toString().equalsIgnoreCase(uiCategory.toString())){
				return true;
			}
		}
		return false;
	}

	public boolean isDMFile(){
		String[] cats = new String[] {"ATTRIBUTE", "GROUP", "POLICY", "RELATIONSHIP", "ROLE", "TYPE"};
		for (int i = 0; i < cats.length; i++) {
			String cat = cats[i];
			if (getCategory().toString().equalsIgnoreCase(cat)){
				return true;
			}
		}
		return false;
	}

    public boolean isJPOFile(){
		String[] cats = new String[] {"JPO"};
		for (int i = 0; i < cats.length; i++) {
			String cat = cats[i];
			if (getCategory().toString().equalsIgnoreCase(cat)){
				return true;
			}
		}
		return false;
    }
}
