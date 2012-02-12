package eclipsematrix.utils;


/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 *
 */
public class ConfigFileUtil {	
	
	public enum CategoryEnum {
		JPO, CHANNEL, COMMAND, FORM, MENU, PORTAL, TABLE, ATTRIBUTE, GROUP, POLICY, RELATIONSHIP, ROLE, TYPE
	}
	
	public enum JPOCategories {
		JPO
	}
	
	public enum UICategories {
		CHANNEL, COMMAND, FORM, MENU, PORTAL, TABLE
	}
	
	public enum DMCategories {
		INTERFACE, GROUP, POLICY, RELATIONSHIP, ROLE, TYPE, STRING, BOOLEAN, INTEGER, REAL, DATE
	}
	
	public enum AttributeCategoryEnum {
		STRING, BOOLEAN, INTEGER, REAL, DATE
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
	
	public static CategoryEnum getCategory(String name) {
		String fileExtension = name.split("\\.")[1];
		//no pur java or tcl file
		if (!fileExtension.equals("java") && !fileExtension.equals("tcl")){
			return null;
		}

		for (CategoryEnum category : CategoryEnum.values()) {
			if (name.contains(category.toString() + "_") || (name.contains("mx" + category.toString()))){
				return category;
			}
		}
		//workaround for Attributes
		for (AttributeCategoryEnum attribCat : AttributeCategoryEnum.values()){
			if (name.contains(attribCat.toString() + "_")){
				return CategoryEnum.ATTRIBUTE;
			}
		}
		return null;
	}
}
