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

		// Request focus on lineInput whenever the dialog is shown
		stage.setOnShown(e -> controller.requestFocus());

		// Execute escButton when Escape key is pressed
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
				controller.fireEscButton();
				event.consume();
			}
		});

		// Intercept stage close request (e.g. clicking 'X' close button) to ask for confirmation
		stage.setOnCloseRequest(event -> {
			if (!controller.confirmClose()) {
				event.consume();
			}
		});
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}
}
