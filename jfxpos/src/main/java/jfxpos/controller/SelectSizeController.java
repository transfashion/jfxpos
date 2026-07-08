package jfxpos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpossyn.model.Item;

import java.util.List;

public class SelectSizeController extends Controller {
	@FXML
	private TableView<Item> sizeTable;

	@FXML
	private TableColumn<Item, String> colArticle;

	@FXML
	private TableColumn<Item, String> colColor;

	@FXML
	private TableColumn<Item, String> colSize;

	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private Item selectedItem = null;
	private final List<Item> items;

	public SelectSizeController(List<Item> items) {
		super(SelectSizeController.class);
		this.items = items;
	}

	@FXML
	public void initialize() {
		colArticle.setCellValueFactory(new PropertyValueFactory<>("itemArt"));
		colColor.setCellValueFactory(new PropertyValueFactory<>("itemCol"));
		colSize.setCellValueFactory(new PropertyValueFactory<>("itemSize"));

		ObservableList<Item> data = FXCollections.observableArrayList(items);
		sizeTable.setItems(data);
		if (!data.isEmpty()) {
			sizeTable.getSelectionModel().select(0);
		}

		btnSelect.setOnAction(e -> confirmSelection());
		btnCancel.setOnAction(e -> closeDialog());

		sizeTable.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		sizeTable.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		javafx.application.Platform.runLater(() -> sizeTable.requestFocus());
	}

	private void confirmSelection() {
		selectedItem = sizeTable.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			closeDialog();
		}
	}

	private void closeDialog() {
		if (sizeTable.getScene() != null && sizeTable.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public Item getSelectedItem() {
		return selectedItem;
	}
}
