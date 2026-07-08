package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jfxpos.Controller;

public class CustDisplayController extends Controller {

	@FXML
	private Label grandTotalValueLabel;

	@FXML
	private Label grandTotalQtyLabel;

	@FXML
	private VBox imageContainer;

	@FXML
	private ImageView imageView;

	@FXML
	private TableView<jfxpos.models.TrxItem> itemTableView;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, jfxpos.models.TrxItem> colItemDescr;

	@FXML
	private TableColumn<jfxpos.models.TrxItem, Integer> colQty;

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
		if (grandTotalQtyLabel != null) {
			grandTotalQtyLabel.setText("0");
		}

		if (colItemDescr != null) {
			colItemDescr.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue()));
			colItemDescr.setCellFactory(column -> new ItemDescrCell());
			colItemDescr.prefWidthProperty().bind(
				itemTableView.widthProperty()
				.subtract(colQty.widthProperty())
				.subtract(colTotal.widthProperty())
				.subtract(15)
			);
		}
		if (colQty != null) {
			colQty.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
		}
		if (colTotal != null) {
			colTotal.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("total"));
			colTotal.setCellFactory(column -> new TableCell<jfxpos.models.TrxItem, java.math.BigDecimal>() {
				@Override
				protected void updateItem(java.math.BigDecimal item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(String.format("%,.0f", item));
						setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
					}
				}
			});
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

	public void setGrandTotalQty(String qty) {
		if (grandTotalQtyLabel != null) {
			javafx.application.Platform.runLater(() -> grandTotalQtyLabel.setText(qty));
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
