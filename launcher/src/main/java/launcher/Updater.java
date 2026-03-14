package launcher;

import javafx.application.Platform;
import javafx.scene.control.Label;

public final class Updater {

	static Label status;

	public static final void update(Label status) {
		Updater.status = status;

		try {
			Platform.runLater(() -> status.setText("Check for Update"));
			UpdateInfo info = getUpdateInfo();

			if (info != null) {
				Platform.runLater(() -> status.setText("Download Update"));
				downloadUpdate(info);

				Platform.runLater(() -> status.setText("Apply Update"));
				updateJar(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Platform.runLater(() -> status.setText("Error on check update"));
		}

	}

	static UpdateInfo getUpdateInfo() throws Exception {
		Thread.sleep(3000); // Simulate some process delay for visibility

		UpdateInfo info = new UpdateInfo();
		return info;
	}

	static void downloadUpdate(UpdateInfo info) throws Exception {
		Thread.sleep(3000); // Simulate some process delay for visibility
	}

	static void updateJar(UpdateInfo info) throws Exception {
		Thread.sleep(3000); // Simulate some process delay for visibility
	}

}
