package eclipsematrix.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;

import eclipsematrix.EclipseMatrix;
import eclipsematrix.entities.MxFile;
import eclipsematrix.entities.MxFile.CategoryEnum;
import eclipsematrix.entities.MxFileList;
import eclipsematrix.preferences.PreferenceConstants;

public class FileUtil {

	protected FileUtil() {
		throw new UnsupportedOperationException();
	}
	
	public static void generateTextFile(MxFileList mxFileList){
		IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
		String logfile = store.getString(PreferenceConstants.P_LOGFILEPATH) + "\\jpoLog.txt";
		File file = new File(logfile);
		PrintWriter o = null;
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, false);
			o = new PrintWriter(fos);
			for (Iterator iterator = mxFileList.iterator(); iterator
					.hasNext();) {
				MxFile mxFile = (MxFile) iterator.next();
				o.println(mxFile.toFileString());					
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (o != null){
			 o.close();
			}
		}		
	}

	
	public static ArrayList<MxFile> getFileData() throws Exception{
		IPreferenceStore store = EclipseMatrix.getDefault().getPreferenceStore();
		String logfile = store.getString(PreferenceConstants.P_LOGFILEPATH) + "\\jpoLog.txt";
		File file = new File(logfile);
		if (file.exists()){
			FileInputStream fis = new FileInputStream(file);
			BufferedReader bufRdr = new BufferedReader(new InputStreamReader(fis));
			ArrayList<MxFile> strArr = new ArrayList<MxFile>();
	
		    for (String line; (line = bufRdr.readLine()) != null;)
		    {
				Boolean changed = Boolean.FALSE;
				String absolutePath = line.split("#")[0];
				String changedStr = line.split("#")[1];
				String categoryStr = line.split("#")[2];
				
				if (changedStr.equals("*")){
					changed = Boolean.TRUE;
				}
				strArr.add(new MxFile(absolutePath, changed, CategoryEnum.valueOf(categoryStr)));
		    }
			bufRdr.close();
			fis.close();
			return strArr;
		} else {
			return new ArrayList<MxFile>();
		}
	}
}
