package eclipsematrix.utils;

import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.MatrixException;
import eclipsematrix.entities.MxFile;

public class CopyOfMQLUtil {
	private static MQLCommand mqlcommand = new MQLCommand();
	private Context ctx = null;

	public CopyOfMQLUtil() {
		try {
			System.err.println("This is only a mock use correct util!");
//			this.ctx = EclipseMatrix.getDefault().getContext();
//			MQLCommand cmd = new MQLCommand();
//			cmd.executeCommand(ctx, "verbose on");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public String mqlCommand(String query) throws MatrixException {
		String strResult = null;
		try {
			if (mqlcommand == null)
				mqlcommand = new MQLCommand();

			boolean executeCommand = mqlcommand.executeCommand(ctx, query);
			ctx.updateClientTasks();

			if (!executeCommand) {
				String strError = mqlcommand.getError();
				int j = strError.length();
				if (j > 0) {
					j--;
					strError = strError.substring(0, j);
				}
				throw new MatrixException(strError);
			}
			strResult = mqlcommand.getResult();
			int i = strResult.length();
			if (i > 0) {
				i--;
				strResult = strResult.substring(0, i);
			}
		} catch (Exception ex) {
			throw new MatrixException(ex.getMessage());
		}
		return strResult;
	}

	public String genericImport(String file) throws Exception {
		MxFile mxFile = new MxFile(file, true);
		String cat = mxFile.getCategory().toString().toLowerCase();
		// test for null cat
		// return mqlCommand("exec prog emxGerLibUpdateRunScripts.tcl -"+ cat
		// +" '"+file +"'");
		return mqlCommand("exec prog emxGerLibUpdate.tcl -mode install -" + cat
				+ " '" + file + "'");
		// todo look for cmd etc
		// return null;
	}
}
