package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxpos.App;
import jfxpos.View;
import jfxpos.controller.CustdisplayController;

public class CustdisplayWindow extends View {
	static final String Title = "Customer Display";
	static final String FXML = RESOURCE_DIR + "/custdisplay.fxml";

	private static CustdisplayWindow instance;

	final Stage stage;
	final CustdisplayController controller;

	public static CustdisplayWindow getInstance() {
		return instance;
	}

	public CustdisplayController getController() {
		return this.controller;
	}

	public CustdisplayWindow() throws Exception {
		this(new Stage());
	}

	public CustdisplayWindow(Stage stage) throws Exception {
		super(CustdisplayWindow.class);
		this.stage = stage;

		if (App.isProd) {
			stage.initStyle(StageStyle.UNDECORATED);
		}

		this.controller = new CustdisplayController();
		instance = this;

		stage.setOnHidden(e -> {
			if (instance == this) {
				instance = null;
			}
		});

		stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (isNowFocused) {
				javafx.stage.Window targetWindow = null;
				java.util.List<javafx.stage.Window> windows = javafx.stage.Window.getWindows();
				for (int i = windows.size() - 1; i >= 0; i--) {
					javafx.stage.Window w = windows.get(i);
					if (w != stage && w.isShowing()) {
						targetWindow = w;
						break;
					}
				}
				if (targetWindow != null) {
					targetWindow.requestFocus();
				}
			}
		});

		Scene scene = loadFxml(FXML, controller);
		setStage(stage);
		stage.setTitle(Title);
		stage.setScene(scene);

		// Set dimensions from FXML pref size
		double minWidth = scene.getRoot().minWidth(-1);
		if (minWidth > 0 && minWidth != Double.NEGATIVE_INFINITY) {
			stage.setMinWidth(minWidth);
		}
		double minHeight = scene.getRoot().minHeight(-1);
		if (minHeight > 0 && minHeight != Double.NEGATIVE_INFINITY) {
			stage.setMinHeight(minHeight);
		}
	}

	@Override
	public void open() {
		if (App.isProd) {
			stage.setMaximized(true);
		}
		stage.show();
	}
}
