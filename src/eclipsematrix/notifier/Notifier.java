package eclipsematrix.notifier;


public class Notifier {

	public static void logError(String title, String message) {
			NotifierDialog
			.notify(title,
					message,
					NotificationType.ERROR);
			
		}
	}

