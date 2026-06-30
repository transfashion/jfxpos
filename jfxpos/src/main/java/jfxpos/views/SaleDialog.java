package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.SaleController;

public class SaleDialog extends View {
	static final String Title = "Point of Sale";
	static final String FXML = RESOURCE_DIR + "/sale.fxml";

	final Stage stage;
	final SaleController controller = new SaleController();

	public SaleDialog(Stage owner, int consoleNumber) throws Exception {
		super(SaleDialog.class);

		Scene scene = loadFxml(FXML, controller);
		controller.setConsoleNumber(consoleNumber);
		stage = createDialogStage(Title + " - Console #" + consoleNumber, scene, owner);
		stage.setResizable(true);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}
}
