package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxpos.Controller;

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
	private TextField barcodeInput;

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

	@FXML
	public void initialize() {
		logger.info("Initializing SaleController...");

		// Close dialog on ESC button click
		if (escButton != null) {
			escButton.setOnAction(e -> {
				if (escButton.getScene() != null && escButton.getScene().getWindow() instanceof Stage stage) {
					stage.close();
				}
			});
		}
	}
}
