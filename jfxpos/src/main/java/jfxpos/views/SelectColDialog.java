package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.SelectColController;
import jfxpossyn.model.Item;

import java.util.List;

public class SelectColDialog extends View {
	static final String Title = "Select Item Color";
	static final String FXML = RESOURCE_DIR + "/saleitem-select-col.fxml";

	final Stage stage;
	final SelectColController controller;

	public SelectColDialog(Stage owner, List<Item> items) throws Exception {
		super(SelectColDialog.class);

		controller = new SelectColController(items);
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
