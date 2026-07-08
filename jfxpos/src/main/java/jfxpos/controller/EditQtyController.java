package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.MessageBox;

public class EditQtyController extends Controller {
	@FXML
	private Label itemIdLabel;

	@FXML
	private Label itemDescrLabel;

	@FXML
	private TextField qtyEditInput;

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	private final long itemId;
	private final String itemDescr;
	private final int initialQty;

	private int newQty;
	private boolean confirmed = false;

	public EditQtyController(long itemId, String itemDescr, int initialQty) {
		super(EditQtyController.class);
		this.itemId = itemId;
		this.itemDescr = itemDescr;
		this.initialQty = initialQty;
		this.newQty = initialQty;
	}

	@FXML
	public void initialize() {
		if (itemIdLabel != null) {
			itemIdLabel.setText(String.valueOf(itemId));
		}
		if (itemDescrLabel != null) {
			itemDescrLabel.setText(itemDescr);
		}
		if (qtyEditInput != null) {
			qtyEditInput.setText(String.valueOf(initialQty));
			qtyEditInput.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
				String newText = change.getControlNewText();
				if (newText.matches("\\d*") && newText.length() <= 2) {
					return change;
				}
				return null;
			}));
			qtyEditInput.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					handleOk();
					event.consume();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					handleCancel();
					event.consume();
				}
			});
		}

		if (okButton != null) {
			okButton.setOnAction(e -> handleOk());
		}
		if (cancelButton != null) {
			cancelButton.setOnAction(e -> handleCancel());
		}

		// Focus and select text when showing
		javafx.application.Platform.runLater(() -> {
			if (qtyEditInput != null) {
				qtyEditInput.requestFocus();
				qtyEditInput.selectAll();
			}
		});
	}

	private void handleOk() {
		try {
			int qty = Integer.parseInt(qtyEditInput.getText().trim());
			if (qty <= 0) {
				MessageBox.info(getStage(), "Quantity harus lebih besar dari 0!", "Warning");
				qtyEditInput.requestFocus();
				qtyEditInput.selectAll();
				return;
			}
			this.newQty = qty;
			this.confirmed = true;
			closeDialog();
		} catch (NumberFormatException e) {
			MessageBox.info(getStage(), "Quantity tidak valid!", "Warning");
			qtyEditInput.requestFocus();
			qtyEditInput.selectAll();
		}
	}

	private void handleCancel() {
		closeDialog();
	}

	private void closeDialog() {
		if (qtyEditInput != null && qtyEditInput.getScene() != null && qtyEditInput.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	private Stage getStage() {
		if (qtyEditInput != null && qtyEditInput.getScene() != null && qtyEditInput.getScene().getWindow() instanceof Stage stage) {
			return stage;
		}
		return null;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public int getNewQty() {
		return newQty;
	}
}
