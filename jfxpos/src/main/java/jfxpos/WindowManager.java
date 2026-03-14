package jfxpos;

import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxpos.controller.LoginController;

public class WindowManager {

	static final String DASHBOARD_TITLE = "Point of Sales";
	static final String RESOURCE_DIR = "";
	static final String FXML_DASHBOARD_AUTHED = "dashboard-authed.fxml";
	static final String FXML_DASHBOARD_UNAUTH = "dashboard-unauth.fxml";
	static final String FXML_LOGIN = "login.fxml";

	static Stage stageDashboard;

	public static FXMLLoader createLoader(String fxml, Object controller) {
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

			return loader;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Stage createWindow(String fxml, String title, Object controller) {
		try {
			FXMLLoader loader = createLoader(fxml, controller);
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			return stage;

		} catch (Exception ex) {
			throw new RuntimeException("Cannot create window: " + fxml, ex);
		}
	}

	public static Stage openDashboardWindow(Stage stage) {
		String fxml = RESOURCE_DIR + "/" + FXML_DASHBOARD_UNAUTH;

		WindowManager.stageDashboard = stage;

		try {
			FXMLLoader loader = createLoader(fxml, null);
			Parent root = loader.load();

			stage.setTitle(DASHBOARD_TITLE);
			stage.setScene(new Scene(root));
			stage.show();

			Platform.runLater(() -> {
				stage.setMaximized(true);
				stage.requestFocus();

				// tampilkan login
				openLoginDialog(stage);
			});

			return stage;
		} catch (Exception ex) {
			throw new RuntimeException("Cannot create window: " + fxml, ex);
		}
	}

	public static Stage openLoginDialog(Stage parentStage) {
		String fxmlPath = RESOURCE_DIR + "/" + FXML_LOGIN;

		try {
			LoginController controller = new LoginController();
			Stage loginStage = createWindow(fxmlPath, "Login", controller);
			loginStage.initOwner(parentStage);
			loginStage.initModality(Modality.APPLICATION_MODAL);
			loginStage.initStyle(StageStyle.UTILITY);
			loginStage.setResizable(false);

			Platform.runLater(() -> {
				loginStage.requestFocus();
			});

			loginStage.showAndWait();

			return loginStage;
		} catch (Exception ex) {
			throw ex;
		}

	}

}
