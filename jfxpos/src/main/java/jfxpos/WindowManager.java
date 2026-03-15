package jfxpos;

import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.controller.DashboardController;
import jfxpos.controller.LoginController;

public class WindowManager {

	static final String DASHBOARD_TITLE = "Point of Sales";
	static final String RESOURCE_DIR = "";
	static final String FXML_DASHBOARD = "dashboard.fxml";
	static final String FXML_LOGIN = "login.fxml";

	static Stage stageMain;

	public static void setMainStage(Stage stage) {
		WindowManager.stageMain = stage;
	}

	public static Scene createScene(String fxml, Object controller) throws Exception {
		try {
			URL fxmlUrl = WindowManager.class.getResource(fxml);
			if (fxmlUrl == null) {
				throw new RuntimeException("FXML not found: " + fxml);
			}

			FXMLLoader loader = new FXMLLoader(fxmlUrl);
			loader.setClassLoader(WindowManager.class.getClassLoader());
			if (controller != null) {
				loader.setController(controller);
			}

			Parent root = loader.load();
			Scene scene = new Scene(root);

			return scene;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Stage createWindow(String fxml, String title, Object controller) {
		try {
			Stage stage = new Stage();
			Scene scene = createScene(fxml, controller);
			stage.setTitle(title);
			stage.setScene(scene);
			return stage;

		} catch (Exception ex) {
			throw new RuntimeException("Cannot create window: " + fxml, ex);
		}
	}

	public static Stage openLogin() throws Exception {
		String fxml = RESOURCE_DIR + "/" + FXML_LOGIN;
		Stage stage = WindowManager.stageMain;

		LoginController controller = new LoginController();
		Scene loginScene = createScene(fxml, controller);
		stage.setTitle(DASHBOARD_TITLE);
		stage.setScene(loginScene);
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.show();

		Platform.runLater(() -> {
			// stage.setMaximized(true);
			stage.requestFocus();

		});

		return stage;

	}

	public static Stage openDashboard() throws Exception {
		String fxml = RESOURCE_DIR + "/" + FXML_DASHBOARD;
		Stage stage = WindowManager.stageMain;

		DashboardController controller = new DashboardController();
		Scene dashboardScene = createScene(fxml, controller);
		stage.setScene(dashboardScene);

		return stage;
	}

}
