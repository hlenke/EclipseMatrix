package eclipsematrix.entities;

import java.util.ArrayList;
import java.util.Iterator;


public class MxFileList extends ArrayList<MxFile> {
	private static final long serialVersionUID = -350118419792464713L;

	/**
	 * Iterate over list and look for given file.
	 * @param mxFile
	 * @return
	 */
	public Boolean isFileInList(MxFile mxFile) {
		for (int i = 0; i < this.size(); i++){
			if (this.get(i).getAbsolutePath().equalsIgnoreCase(mxFile.getAbsolutePath())){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public MxFile getMxFileFromList(MxFile mxFile){
		for (int i = 0; i < this.size(); i++){
			if (this.get(i).getAbsolutePath().equalsIgnoreCase(mxFile.getAbsolutePath())){
				return this.get(i);
			}
		}
		return null;
	}
	
	public Boolean removeFileFromList(MxFile mxFile){
		for (int i = 0; i < this.size(); i++){
			if (this.get(i).getAbsolutePath().equalsIgnoreCase(mxFile.getAbsolutePath())){
				this.remove(i);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
    public MxFileList sortUI() {
    	String[] cats = new String[] {"COMMAND", "MENU", "FORM", "TABLE", "CHANNEL","PORTAL"};
    	MxFileList sorted = new MxFileList();
    	for (int i = 0; i < cats.length; i++) {
			String cat = cats[i];
			for (Iterator iterator = this.iterator(); iterator.hasNext();) {
				MxFile mxFile = (MxFile) iterator.next();
				if(mxFile.getCategory().toString().equalsIgnoreCase(cat)){
					sorted.add(mxFile);
				}
			}
		}
    	return sorted;
    }
    
    public MxFileList sortDM() {
    	String[] cats = new String[] {"DIMENSION", "ATTRIBUTE", "TYPE", "POLICY", "RELATIONSHIP","INTERFACE", "ROLE"};
    	MxFileList sorted = new MxFileList();
    	for (int i = 0; i < cats.length; i++) {
			String cat = cats[i];
			for (Iterator iterator = this.iterator(); iterator.hasNext();) {
				MxFile mxFile = (MxFile) iterator.next();
				if(mxFile.getCategory().toString().equalsIgnoreCase(cat)){
					sorted.add(mxFile);
				}
			}
		}
    	return sorted;
    }
}
