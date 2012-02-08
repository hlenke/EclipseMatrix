//package eclipsematrix.utils;
//
//import java.io.File;
//
//import org.eclipse.ui.PlatformUI;
//
//import com.sun.jna.examples.FileMonitor;
//import com.sun.jna.examples.FileMonitor.FileListener;
//
//import eclipsematrix.views.CheckInView;
//
//public class JPOFileListener implements FileListener {
//	
//	public void fileChanged(FileMonitor.FileEvent fEvent) {
//		System.out.println("type " + fEvent.getType());
//		System.out.println("file " + fEvent.getFile().getName());
//		final File afile = fEvent.getFile();
//		final FileMonitor.FileEvent afEvent = fEvent;
//		//exec Workbench outside UI Thread
//			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//				public void run() {
//					CheckInView view = (CheckInView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("eclipsematrix.views.CheckInView");
//						if (view != null) {
//							try {
//								if(afEvent.getType() == FileMonitor.FILE_MODIFIED){
//									view.fileChanged(afile);
//								} else if((afEvent.getType() == FileMonitor.FILE_CREATED) || (afEvent.getType() == FileMonitor.FILE_NAME_CHANGED_NEW)){
//									view.fileCreated(afile);
//								} else if((afEvent.getType() == FileMonitor.FILE_DELETED) || (afEvent.getType() == FileMonitor.FILE_NAME_CHANGED_OLD)){
//									view.fileDeleted(afile);
//								}
//							}catch (Exception e) {
//								System.out.println(e.getMessage());
//							}
//						}
//					}
//			});
//	}
//}
