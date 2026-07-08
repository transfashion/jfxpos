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
import jfxpos.models.PromoItem;
import jfxpos.repository.PromoItemRepository;

import java.util.List;

public class PromoItemController extends Controller {
	@FXML
	private TableView<PromoItem> promoItemTable;

	@FXML
	private TableColumn<PromoItem, Integer> colId;

	@FXML
	private TableColumn<PromoItem, String> colNote;

	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private final PromoItemRepository promoItemRepo = new PromoItemRepository();
	private PromoItem selectedPromoItem = null;

	public PromoItemController() {
		super(PromoItemController.class);
	}

	@FXML
	public void initialize() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

		loadPromos();

		// Handle select button action
		btnSelect.setOnAction(e -> confirmSelection());

		// Handle cancel button action
		btnCancel.setOnAction(e -> closeDialog());

		// Double-click row to select
		promoItemTable.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Key events within TableView
		promoItemTable.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		// Auto-focus table on load
		javafx.application.Platform.runLater(() -> promoItemTable.requestFocus());
	}

	private void loadPromos() {
		try {
			List<PromoItem> list = promoItemRepo.findAllActive();
			ObservableList<PromoItem> data = FXCollections.observableArrayList(list);
			promoItemTable.setItems(data);
			if (!data.isEmpty()) {
				promoItemTable.getSelectionModel().select(0);
			}
		} catch (Exception e) {
			logger.severe("Failed to load promo items: " + e.getMessage());
		}
	}

	private void confirmSelection() {
		selectedPromoItem = promoItemTable.getSelectionModel().getSelectedItem();
		if (selectedPromoItem != null) {
			closeDialog();
		}
	}

	private void closeDialog() {
		if (promoItemTable.getScene() != null && promoItemTable.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public PromoItem getSelectedPromoItem() {
		return selectedPromoItem;
	}

	public void selectPromoItemById(int promoId) {
		if (promoItemTable != null) {
			for (PromoItem promo : promoItemTable.getItems()) {
				if (promo.getId() == promoId) {
					promoItemTable.getSelectionModel().select(promo);
					break;
				}
			}
		}
	}
}
