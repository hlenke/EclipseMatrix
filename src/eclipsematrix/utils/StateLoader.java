package eclipsematrix.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import eclipsematrix.entities.ConfigFileRecord;

public class StateLoader {

	private static final String SAVE_FILE_NAME = "savefile.sav";

	private File loadFile() {
		File file = null;
		try {
			file = new File(eclipsematrix.EclipseMatrix.getDefault()
					.getStateLocation().toFile(), SAVE_FILE_NAME);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public List<ConfigFileRecord> loadState() throws Exception {
		File saveFile = loadFile();

		if (saveFile.exists()) {
			FileInputStream fis = new FileInputStream(saveFile);
			BufferedReader bufRdr = new BufferedReader(new InputStreamReader(
					fis));
			LinkedList<ConfigFileRecord> fileList = new LinkedList<ConfigFileRecord>();

			for (String line; (line = bufRdr.readLine()) != null;) {
				fileList.add(unSerialize(line));
			}
			bufRdr.close();
			fis.close();
			return fileList;
		} else {
			return new LinkedList<ConfigFileRecord>();
		}
	}
	
	public void generateTextFile(List<ConfigFileRecord> mxFileList){
		File file = loadFile();
		PrintWriter o = null;
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, false);
			o = new PrintWriter(fos);
			for (ConfigFileRecord configFileRecord : mxFileList) {
				o.println(serialize(configFileRecord));					
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (o != null){
			 o.close();
			}
		}		
	}
	
	private String serialize(ConfigFileRecord record) {
		return record.getPath() + "#" + record.isChanged();
	}
	
	private ConfigFileRecord unSerialize(String line) {
		String absolutePath = line.split("#")[0];
		String changedStr = line.split("#")[1];
		Boolean	changed = Boolean.parseBoolean(changedStr);
		return new ConfigFileRecord(absolutePath, changed);
	}
}

