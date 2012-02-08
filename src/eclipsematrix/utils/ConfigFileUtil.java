package eclipsematrix.utils;


public class ConfigFileUtil {	
	
	public enum JPOCategories {
		JPO
	}
	
	public enum UICategories {
		CHANNEL, COMMAND, FORM, MENU, PORTAL, TABLE
	}
	
	public enum DMCategories {
		INTERFACE, GROUP, POLICY, RELATIONSHIP, ROLE, TYPE, STRING, BOOLEAN, INTEGER, REAL, DATE
	}
	
	private ConfigFileUtil() {
		
	}
	
	public static boolean isJPOFile(String fileName){
		for (JPOCategories jpoEnum : JPOCategories.values()) {
			if (fileName.contains("mx" + jpoEnum.toString())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDMFile(String fileName){
		for (DMCategories dmEnum : DMCategories.values()) {
			if (fileName.contains(dmEnum.toString() + "_")){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUIFile(String fileName) {
		for (UICategories uiEnum : UICategories.values()) {
			if (fileName.contains(uiEnum.toString() + "_")){
				return true;
			}
		}
		return false;
	}

	public static boolean isValidConfigFile(String fileName) {
		if (fileName.endsWith(".tcl") || fileName.endsWith(".java")) {
			return true;
		}
		return false;
	}
}
