package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.ConfigController;

public class ConfigDialog extends View {
	static final String Title = "Configuration";
	static final String FXML = RESOURCE_DIR + "/config2.fxml";

	final Stage stage;
	final ConfigController controller = new ConfigController();

	public ConfigDialog(Stage owner) throws Exception {
		super(ConfigDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

}
