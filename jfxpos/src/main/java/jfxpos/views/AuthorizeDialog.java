package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.AuthorizeController;

public class AuthorizeDialog extends View {
	static final String Title = "Authorize";
	static final String FXML = RESOURCE_DIR + "/authorize.fxml";

	final Stage stage;
	final AuthorizeController controller = new AuthorizeController();

	public AuthorizeDialog(Stage owner) throws Exception {
		super(AuthorizeDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}
}
