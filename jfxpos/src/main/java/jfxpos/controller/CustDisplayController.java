package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jfxpos.Controller;

public class CustDisplayController extends Controller {

	@FXML
	private Label grandTotalValueLabel;

	@FXML
	private VBox imageContainer;

	@FXML
	private ImageView imageView;

	@FXML
	private Label customerNameLabel;

	@FXML
	private Label dateLabel;

	@FXML
	private Label timeLabel;

	public CustDisplayController() {
		super(CustDisplayController.class);
	}

	@FXML
	public void initialize() {
		if (imageView != null && imageContainer != null) {
			imageView.fitWidthProperty().bind(imageContainer.widthProperty());
			imageView.fitHeightProperty().bind(imageContainer.heightProperty());
		}
		if (customerNameLabel != null) {
			customerNameLabel.setText("");
		}
		if (grandTotalValueLabel != null) {
			grandTotalValueLabel.setText("0");
		}
	}

	public void setGrandTotal(String grandTotal) {
		if (grandTotalValueLabel != null) {
			javafx.application.Platform.runLater(() -> grandTotalValueLabel.setText(grandTotal));
		}
	}

	public void setCustomerName(String customerName) {
		if (customerNameLabel != null) {
			javafx.application.Platform.runLater(() -> customerNameLabel.setText(customerName));
		}
	}

	public void updateDateTime(String dateText, String timeText) {
		javafx.application.Platform.runLater(() -> {
			if (dateLabel != null) {
				dateLabel.setText(dateText);
			}
			if (timeLabel != null) {
				timeLabel.setText(timeText);
			}
		});
	}
}
