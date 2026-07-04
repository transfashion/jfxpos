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
import jfxpos.models.PromoPayment;
import jfxpos.repository.PromoPaymentRepository;

import java.math.BigDecimal;
import java.util.List;

public class PromoPaymentController extends Controller {
	@FXML
	private TableView<PromoPayment> promoPaymentTable;

	@FXML
	private TableColumn<PromoPayment, Integer> colId;

	@FXML
	private TableColumn<PromoPayment, String> colNote;

	@FXML
	private TableColumn<PromoPayment, BigDecimal> colValue;

	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private final PromoPaymentRepository promoPaymentRepo = new PromoPaymentRepository();
	private PromoPayment selectedPromoPayment = null;

	public PromoPaymentController() {
		super(PromoPaymentController.class);
	}

	@FXML
	public void initialize() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
		colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

		loadPromos();

		// Handle select button action
		btnSelect.setOnAction(e -> confirmSelection());

		// Handle cancel button action
		btnCancel.setOnAction(e -> closeDialog());

		// Double-click row to select
		promoPaymentTable.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Key events within TableView
		promoPaymentTable.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		// Auto-focus table on load
		javafx.application.Platform.runLater(() -> promoPaymentTable.requestFocus());
	}

	private void loadPromos() {
		try {
			List<PromoPayment> list = promoPaymentRepo.findAll();
			ObservableList<PromoPayment> data = FXCollections.observableArrayList(list);
			promoPaymentTable.setItems(data);
			if (!data.isEmpty()) {
				promoPaymentTable.getSelectionModel().select(0);
			}
		} catch (Exception e) {
			logger.severe("Failed to load promo payments: " + e.getMessage());
		}
	}

	private void confirmSelection() {
		selectedPromoPayment = promoPaymentTable.getSelectionModel().getSelectedItem();
		if (selectedPromoPayment != null) {
			closeDialog();
		}
	}

	private void closeDialog() {
		if (promoPaymentTable.getScene() != null && promoPaymentTable.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public PromoPayment getSelectedPromoPayment() {
		return selectedPromoPayment;
	}

	public void selectPromoPaymentById(int promoId) {
		if (promoPaymentTable != null) {
			for (PromoPayment promo : promoPaymentTable.getItems()) {
				if (promo.getId() == promoId) {
					promoPaymentTable.getSelectionModel().select(promo);
					break;
				}
			}
		}
	}
}
