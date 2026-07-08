package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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
	private TableView<jfxpos.models.TrxItem> itemTableView;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, String> colDescr;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, Integer> colQty;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, java.math.BigDecimal> colPrice;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, java.math.BigDecimal> colTotal;

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

		if (colDescr != null) {
			colDescr.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemDescr"));
		}
		if (colQty != null) {
			colQty.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
		}
		if (colPrice != null) {
			colPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemPrice"));
		}
		if (colTotal != null) {
			colTotal.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("total"));
		}
	}

	public void setItems(java.util.List<jfxpos.models.TrxItem> items) {
		if (itemTableView != null) {
			javafx.application.Platform.runLater(() -> {
				itemTableView.getItems().setAll(items);
				int lastIndex = itemTableView.getItems().size() - 1;
				if (lastIndex >= 0) {
					itemTableView.getSelectionModel().select(lastIndex);
					itemTableView.scrollTo(lastIndex);
				}
			});
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

	public void selectRow(int index) {
		if (itemTableView != null) {
			javafx.application.Platform.runLater(() -> {
				if (index >= 0 && index < itemTableView.getItems().size()) {
					itemTableView.getSelectionModel().select(index);
					itemTableView.scrollTo(index);
				} else {
					itemTableView.getSelectionModel().clearSelection();
				}
			});
		}
	}
}
