package jfxpos.views;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.App;
import jfxpos.View;
import jfxpos.controller.DashboardController;
import jfxpos.controller.LoginController;

public class Dashboard extends View {
	static final String Title = "JFX Point of Sales";
	static final String FXML_LOGIN = RESOURCE_DIR + "/login.fxml";
	static final String FXML_DASHBOARD = RESOURCE_DIR + "/dashboard.fxml";

	final Stage stage;

	public Dashboard(Stage stage) {
		super(Dashboard.class);

		setStage(stage);
		stage.setTitle(Title);
		stage.setMinWidth(800);
		stage.setMinHeight(600);

		this.stage = stage;
	}

	public void setLoginView() throws Exception {
		Scene scene = loadFxml(FXML_LOGIN, new LoginController(this));
		stage.setScene(scene);
	}

	public void setDashboardView() throws Exception {
		Scene scene = loadFxml(FXML_DASHBOARD, new DashboardController());
		stage.setScene(scene);
	}

	@Override
	public void open() {
		stage.show();
		Platform.runLater(() -> {
			if (App.isProd) {
				stage.setMaximized(true);
			}
			stage.centerOnScreen();
			stage.requestFocus();
		});
	}

}
