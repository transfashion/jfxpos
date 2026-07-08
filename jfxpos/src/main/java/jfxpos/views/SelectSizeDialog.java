package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.SelectSizeController;
import jfxpossyn.model.Item;

import java.util.List;

public class SelectSizeDialog extends View {
	static final String Title = "Select Item Size";
	static final String FXML = RESOURCE_DIR + "/saleitem-select-size.fxml";

	final Stage stage;
	final SelectSizeController controller;

	public SelectSizeDialog(Stage owner, List<Item> items) throws Exception {
		super(SelectSizeDialog.class);

		controller = new SelectSizeController(items);
		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public Item getSelectedItem() {
		return controller.getSelectedItem();
	}
}
