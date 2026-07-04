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
import jfxpos.models.PromoNextTx;
import jfxpos.repository.PromoNextTxRepository;

import java.util.List;

public class PromoNextTxController extends Controller {
	@FXML
	private TableView<PromoNextTx> promoNextTxTable;

	@FXML
	private TableColumn<PromoNextTx, Integer> colId;

	@FXML
	private TableColumn<PromoNextTx, String> colNote;

	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private final PromoNextTxRepository promoNextTxRepo = new PromoNextTxRepository();
	private PromoNextTx selectedPromoNextTx = null;

	public PromoNextTxController() {
		super(PromoNextTxController.class);
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
		promoNextTxTable.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Key events within TableView
		promoNextTxTable.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		// Auto-focus table on load
		javafx.application.Platform.runLater(() -> promoNextTxTable.requestFocus());
	}

	private void loadPromos() {
		try {
			List<PromoNextTx> list = promoNextTxRepo.findAll();
			ObservableList<PromoNextTx> data = FXCollections.observableArrayList(list);
			promoNextTxTable.setItems(data);
			if (!data.isEmpty()) {
				promoNextTxTable.getSelectionModel().select(0);
			}
		} catch (Exception e) {
			logger.severe("Failed to load promo next transactions: " + e.getMessage());
		}
	}

	private void confirmSelection() {
		selectedPromoNextTx = promoNextTxTable.getSelectionModel().getSelectedItem();
		if (selectedPromoNextTx != null) {
			closeDialog();
		}
	}

	private void closeDialog() {
		if (promoNextTxTable.getScene() != null && promoNextTxTable.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public PromoNextTx getSelectedPromoNextTx() {
		return selectedPromoNextTx;
	}

	public void selectPromoNextTxById(int promoId) {
		if (promoNextTxTable != null) {
			for (PromoNextTx promo : promoNextTxTable.getItems()) {
				if (promo.getId() == promoId) {
					promoNextTxTable.getSelectionModel().select(promo);
					break;
				}
			}
		}
	}
}
