package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.EditQtyController;

public class EditQtyDialog extends View {
	static final String Title = "Edit Quantity";
	static final String FXML = RESOURCE_DIR + "/editqty.fxml";

	final Stage stage;
	final EditQtyController controller;

	public EditQtyDialog(Stage owner, long itemId, String itemDescr, int initialQty) throws Exception {
		super(EditQtyDialog.class);

		controller = new EditQtyController(itemId, itemDescr, initialQty);
		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public boolean isConfirmed() {
		return controller.isConfirmed();
	}

	public int getNewQty() {
		return controller.getNewQty();
	}
}
