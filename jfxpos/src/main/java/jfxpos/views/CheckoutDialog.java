package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.App;
import jfxpos.View;
import jfxpos.controller.CheckoutController;
import jfxpos.models.Trx;

public class CheckoutDialog extends View {
	static final String Title = "Checkout";
	static final String FXML = RESOURCE_DIR + "/checkout.fxml";

	final Stage stage;
	final CheckoutController controller;

	public CheckoutDialog(Stage owner, Trx trx) throws Exception {
		super(CheckoutDialog.class);

		this.controller = new CheckoutController(trx);
		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(true);

		if (App.isProd) {
			stage.setMaximized(true);
		}
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}
}
