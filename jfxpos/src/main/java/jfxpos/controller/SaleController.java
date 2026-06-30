package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxpos.Controller;
import jfxpos.util.MessageBox;

public class SaleController extends Controller {

	@FXML
	private Label storeNameLabel;

	@FXML
	private Label storeInfoLabel;

	@FXML
	private Label dateLabel;

	@FXML
	private Label timeLabel;

	@FXML
	private ImageView searchModeImage;

	@FXML
	private TextField lineInput;

	@FXML
	private Label itemDescriptionLabel;

	@FXML
	private Label itemPriceLabel;

	@FXML
	private TableView<?> itemTable;

	@FXML
	private Label subtotalValueLabel;

	@FXML
	private Label subtotalQtyLabel;

	@FXML
	private Label customerIdLabel;

	@FXML
	private Label customerTypeLabel;

	@FXML
	private Label customerNameLabel;

	@FXML
	private Button btnSearchCustomer;

	@FXML
	private Button btnVipCustomer;

	@FXML
	private Button btnRegisterCustomer;

	@FXML
	private Button btnClearCustomer;

	@FXML
	private Button f1Button;

	@FXML
	private Button f2Button;

	@FXML
	private Button f3Button;

	@FXML
	private Button f4Button;

	@FXML
	private Button f5Button;

	@FXML
	private Button f6Button;

	@FXML
	private Button f7Button;

	@FXML
	private Button f8Button;

	@FXML
	private Button f9Button;

	@FXML
	private Button f10Button;

	@FXML
	private Button f11Button;

	@FXML
	private Button f12Button;

	@FXML
	private Button escButton;

	private int consoleNumber;
	private jfxpos.models.InputSearchMode currentSearchMode = jfxpos.models.InputSearchMode.BARCODE;

	public SaleController() {
		super(SaleController.class);
	}

	public void setConsoleNumber(int consoleNumber) {
		this.consoleNumber = consoleNumber;
		logger.info("SaleController configured for Console #" + consoleNumber);
	}

	public int getConsoleNumber() {
		return consoleNumber;
	}

	public jfxpos.models.InputSearchMode getCurrentSearchMode() {
		return currentSearchMode;
	}

	@FXML
	public void initialize() {
		logger.info("Initializing SaleController...");

		// Set initial prompt text
		if (lineInput != null) {
			lineInput.setPromptText(currentSearchMode.getPrompt());
			lineInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal) {
					javafx.application.Platform.runLater(() -> {
						lineInput.deselect();
						lineInput.end();
					});
				}
			});
		}

		updateSearchModeImage();

		// Rotate search mode on F1 button click
		if (f1Button != null) {
			f1Button.setOnAction(e -> rotateSearchMode());
		}

		// Close dialog on ESC button click
		if (escButton != null) {
			escButton.setOnAction(e -> {
				if (escButton.getScene() != null && escButton.getScene().getWindow() instanceof Stage stage) {
					if (confirmClose()) {
						stage.close();
					}
				}
			});
		}
	}

	private void updateSearchModeImage() {
		if (searchModeImage != null && currentSearchMode.getImage() != null) {
			try {
				String imagePath = "/" + currentSearchMode.getImage();
				java.io.InputStream is = getClass().getResourceAsStream(imagePath);
				if (is != null) {
					Image img = new Image(is);
					searchModeImage.setImage(img);
				} else {
					logger.warning("Search mode image not found: " + imagePath);
				}
			} catch (Exception e) {
				logger.severe("Failed to load search mode image: " + e.getMessage());
			}
		}
	}

	private void rotateSearchMode() {
		jfxpos.models.InputSearchMode[] modes = jfxpos.models.InputSearchMode.values();
		int nextOrdinal = (currentSearchMode.ordinal() + 1) % modes.length;
		currentSearchMode = modes[nextOrdinal];
		if (lineInput != null) {
			lineInput.setPromptText(currentSearchMode.getPrompt());
		}
		updateSearchModeImage();
		logger.info("Search mode changed to: " + currentSearchMode);
	}

	public boolean confirmClose() {
		Stage stage = null;
		if (escButton != null && escButton.getScene() != null) {
			stage = (Stage) escButton.getScene().getWindow();
		}
		return MessageBox.confirm(stage, "Apakah akan menutup console ini?");
	}

	public void requestFocus() {
		if (lineInput != null) {
			javafx.application.Platform.runLater(() -> lineInput.requestFocus());
		}
	}

	public boolean isLineInputFocused() {
		return lineInput != null && lineInput.isFocused();
	}

	public boolean isItemTableFocused() {
		return itemTable != null && itemTable.isFocused();
	}

	public void focusItemTable() {
		if (itemTable != null) {
			itemTable.requestFocus();
			if (itemTable.getSelectionModel().getSelectedIndex() < 0 && !itemTable.getItems().isEmpty()) {
				itemTable.getSelectionModel().select(0);
			}
		}
	}

	public void appendLineInput(String text) {
		if (lineInput != null) {
			lineInput.appendText(text);
			lineInput.positionCaret(lineInput.getText().length());
		}
	}

	public void handleLeftArrow() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				if (pos > 0) {
					lineInput.positionCaret(pos - 1);
				}
			});
		}
	}

	public void handleRightArrow() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				if (pos < lineInput.getText().length()) {
					lineInput.positionCaret(pos + 1);
				}
			});
		}
	}

	public void handleBackspace() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				String text = lineInput.getText();
				if (pos > 0 && text != null && !text.isEmpty()) {
					lineInput.deleteText(pos - 1, pos);
				}
			});
		}
	}

	public void fireF1Button() {
		if (f1Button != null) {
			f1Button.fire();
		}
	}

	public void fireEscButton() {
		if (escButton != null) {
			escButton.fire();
		}
	}
}
