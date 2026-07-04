package jfxpos.views;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxpos.App;
import jfxpos.View;
import jfxpos.controller.DashboardController;
import jfxpos.controller.LoginController;

public class DashboardWindow extends View {
	static final String Title = "JFX Point of Sales";
	static final String FXML_LOGIN = RESOURCE_DIR + "/login.fxml";
	static final String FXML_DASHBOARD = RESOURCE_DIR + "/dashboard.fxml";

	final Stage stage;
	private CustDisplayWindow custdisplayWindow;

	public DashboardWindow(Stage stage) {
		super(DashboardWindow.class);

		setStage(stage);
		stage.setTitle(Title);
		stage.setMinWidth(800);
		stage.setMinHeight(600);

		this.stage = stage;

		stage.setOnCloseRequest(event -> {
			if (custdisplayWindow != null && custdisplayWindow.getStage() != null) {
				custdisplayWindow.getStage().close();
			}
		});

		clockTick(1);
	}

	private void clockTick(int minute) {
		java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter
				.ofPattern("EEEE, d MMMM yyyy", java.util.Locale.of("id", "ID"));
		java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

		javafx.animation.Timeline clock = new javafx.animation.Timeline(
				new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, e -> {
					java.time.LocalDateTime now = java.time.LocalDateTime.now();
					String dateStr = now.format(dateFormatter);
					String timeStr = now.format(timeFormatter);

					// Update active SaleController if any
					jfxpos.controller.SaleController activeController = jfxpos.controller.SaleController.getActiveController();
					if (activeController != null) {
						activeController.updateDateTime(dateStr, timeStr);
					}

					// Update CustDisplayWindow
					if (custdisplayWindow != null) {
						custdisplayWindow.updateDateTime(dateStr, timeStr);
					}
				}),
				new javafx.animation.KeyFrame(javafx.util.Duration.minutes(minute)));
		clock.setCycleCount(javafx.animation.Animation.INDEFINITE);
		clock.play();
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
			} else if (App.isDev) {
				stage.setX(0);
				stage.setY(0);
			}

			try {
				showCustomerDisplay();
				stage.requestFocus();
			} catch (Exception e) {
				logger.severe("Failed to show customer display: " + e.getMessage());
			}
		});
	}

	private void showCustomerDisplay() throws Exception {
		if (custdisplayWindow == null) {
			custdisplayWindow = new CustDisplayWindow();
		}

		if (Screen.getScreens().size() > 1) {
			Screen secondaryScreen = Screen.getScreens().get(1);
			Rectangle2D bounds = secondaryScreen.getVisualBounds();

			Stage custStage = custdisplayWindow.getStage();
			custStage.setX(bounds.getMinX());
			custStage.setY(bounds.getMinY());
			custStage.setWidth(bounds.getWidth());
			custStage.setHeight(bounds.getHeight());
			custdisplayWindow.open();
		} else {
			if (App.isDev) {
				custdisplayWindow.open();
			}
		}

		// Push initial date/time to the newly opened customer display
		java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter
				.ofPattern("EEEE, d MMMM yyyy", java.util.Locale.of("id", "ID"));
		java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
		java.time.LocalDateTime now = java.time.LocalDateTime.now();
		custdisplayWindow.updateDateTime(now.format(dateFormatter), now.format(timeFormatter));
	}

}
