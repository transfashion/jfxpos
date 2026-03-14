package jfxpos;

import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.controller.DashboardController;

public class WindowManager {

	static final String DASHBOARD_TITLE = "Point of Sales";
	static final String RESOURCE_DIR = "";
	static final String FXML_DASHBOARD = "dashboard.fxml";

	public static Stage createWindow(String fxml, String title, Object controller) {
		try {

			URL fxmlUrl = WindowManager.class.getResource(fxml);
			if (fxmlUrl == null) {
				throw new RuntimeException("FXML not found: " + fxml);
			}

			FXMLLoader loader = new FXMLLoader(fxmlUrl);

			loader.setClassLoader(WindowManager.class.getClassLoader());
			loader.setController(controller);

			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			return stage;

		} catch (Exception e) {
			throw new RuntimeException("Cannot create window: " + fxml, e);
		}
	}

	public static Stage openDashboardWindow(Stage stage) {
		String fxmlPath = RESOURCE_DIR + "/" + FXML_DASHBOARD;

		try {
			URL fxmlUrl = WindowManager.class.getResource(fxmlPath);
			if (fxmlUrl == null) {
				throw new RuntimeException("FXML not found: " + fxmlPath);
			}

			FXMLLoader loader = new FXMLLoader(fxmlUrl);

			loader.setClassLoader(WindowManager.class.getClassLoader());
			loader.setController(new DashboardController());
			Parent root = loader.load();

			stage.setTitle(DASHBOARD_TITLE);
			stage.setScene(new Scene(root));
			stage.show();

			Platform.runLater(() -> {
				stage.setMaximized(true);
				stage.requestFocus();

				// tampilkan login

			});
			return stage;
		} catch (Exception e) {
			throw new RuntimeException("Cannot create window: " + fxmlPath, e);
		}
	}

}
