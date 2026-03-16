package jfxpos.util;

import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import jfxpos.controller.ConfigController;
import jfxpos.controller.DashboardController;
import jfxpos.controller.LoginController;

public class WindowManager {

	static final String DASHBOARD_TITLE = "Point of Sales";
	static final String RESOURCE_DIR = "";
	static final String FXML_DASHBOARD = "dashboard.fxml";
	static final String FXML_LOGIN = "login.fxml";
	static final String FXML_CONFIG = "config.fxml";

	static Stage stageMain;

	public static void setMainStage(Stage stage) {
		WindowManager.stageMain = stage;
	}

	public static <T> FxView<T> loadView(String fxml, T controller) throws Exception {
		URL url = WindowManager.class.getResource(fxml);

		FXMLLoader loader = new FXMLLoader(url);
		loader.setClassLoader(WindowManager.class.getClassLoader());

		if (controller != null) {
			loader.setController(controller);
		}

		Parent root = loader.load();
		return new FxView<>(root, controller);
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
			Scene scene = new Scene(root, root.prefWidth(-1), root.prefHeight(-1));

			return scene;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Stage createDialog(Stage owner, Parent root, String title) {

		Stage stage = new Stage();
		Scene scene = new Scene(root);

		stage.setTitle(title);
		stage.initOwner(owner);
		stage.initModality(Modality.WINDOW_MODAL);

		stage.setScene(scene);

		// penting untuk memastikan ukuran benar
		stage.sizeToScene();
		stage.centerOnScreen();

		stage.setResizable(false);
		stage.setMinWidth(root.minWidth(-1));
		stage.setMinHeight(root.minHeight(-1));
		stage.setWidth(root.prefWidth(-1));
		stage.setHeight(root.prefHeight(-1));
		stage.setMaxWidth(root.maxWidth(-1));
		stage.setMaxHeight(root.maxHeight(-1));

		return stage;
	}

	public static void openLogin() throws Exception {
		String fxml = RESOURCE_DIR + "/" + FXML_LOGIN;
		Stage stage = WindowManager.stageMain;

		LoginController controller = new LoginController();
		FxView<LoginController> view = loadView(fxml, controller);

		Scene scene = new Scene(view.root);

		stage.setTitle(DASHBOARD_TITLE);
		stage.setScene(scene);
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.show();

		Platform.runLater(() -> {
			// stage.setMaximized(true);
			stage.centerOnScreen();
			stage.requestFocus();
		});
	}

	public static Stage openDashboard() throws Exception {
		String fxml = RESOURCE_DIR + "/" + FXML_DASHBOARD;
		Stage stage = WindowManager.stageMain;

		DashboardController controller = new DashboardController();

		Scene scene = createScene(fxml, controller);
		stage.setScene(scene);
		controller.setStage(stage);

		return stage;
	}

	public static void openConfig() throws Exception {
		String fxml = RESOURCE_DIR + "/" + FXML_CONFIG;

		ConfigController controller = new ConfigController();
		FxView<ConfigController> view = loadView(fxml, controller);

		Stage stage = createDialog(stageMain, view.root, "Configuration");
		controller.setStage(stage);

		stage.showAndWait();
	}

}
