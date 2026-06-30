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
	private boolean LINE_INPUT_MODE = true;

	public boolean isLINE_INPUT_MODE() {
		return LINE_INPUT_MODE;
	}

	public void setLINE_INPUT_MODE(boolean LINE_INPUT_MODE) {
		this.LINE_INPUT_MODE = LINE_INPUT_MODE;
	}

	public SaleDialog(Stage owner, int consoleNumber) throws Exception {
		super(SaleDialog.class);

		Scene scene = loadFxml(FXML, controller);
		controller.setConsoleNumber(consoleNumber);
		stage = createDialogStage(Title + " - Console #" + consoleNumber, scene, owner);
		stage.setResizable(true);

		// Request focus on lineInput whenever the dialog is shown
		stage.setOnShown(e -> controller.requestFocus());

		// Redirect key typed to lineInput if LINE_INPUT_MODE is active and focus is elsewhere
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
			if (LINE_INPUT_MODE && !controller.isLineInputFocused()) {
				String character = event.getCharacter();
				if (character != null && character.length() == 1) {
					char c = character.charAt(0);
					// Filter printable characters (exclude control characters)
					if (c >= 32 && c != 127) {
						controller.requestFocus();
						controller.appendLineInput(character);
						event.consume();
					}
				}
			}
		});

		// Execute escButton when Escape key is pressed
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
				controller.fireEscButton();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F1) {
				controller.fireF1Button();
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
