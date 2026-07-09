package jfxpos.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.models.Salesperson;
import jfxpos.repository.SalespersonRepository;
import jfxpos.util.MessageBox;

import java.util.List;
import java.util.logging.Logger;

public class SalespersonController extends Controller {

	private static final Logger logger = Logger.getLogger(SalespersonController.class.getName());

	@FXML
	private TextField searchInput;

	@FXML
	private Button submitButton;

	@FXML
	private TableView<Salesperson> tableViewResult;

	@FXML
	private TableColumn<Salesperson, String> colNik;

	@FXML
	private TableColumn<Salesperson, String> colName;

	@FXML
	private TableColumn<Salesperson, Integer> colBrand;

	@FXML
	private Button selectButton;

	@FXML
	private Button cancelButton;

	private final SalespersonRepository salespersonRepository = new SalespersonRepository();
	private Salesperson selectedSalesperson = null;

	public SalespersonController() {
		super(SalespersonController.class);
	}

	@FXML
	public void initialize() {
		// Configure TableView columns
		colNik.setCellValueFactory(new PropertyValueFactory<>("salespersonNik"));
		colName.setCellValueFactory(new PropertyValueFactory<>("salespersonName"));
		colBrand.setCellValueFactory(new PropertyValueFactory<>("brandId"));

		// Event handlers
		submitButton.setOnAction(e -> handleSearch());
		selectButton.setOnAction(e -> confirmSelection());
		cancelButton.setOnAction(e -> closeDialog());

		// Double-click row to select
		tableViewResult.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Move cursor to the end when searchInput gains focus
		searchInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				Platform.runLater(() -> {
					searchInput.deselect();
					searchInput.end();
				});
			}
		});

		// Keyboard controls and shortcuts on scene
		submitButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
					KeyCode code = event.getCode();

					if (code == KeyCode.ESCAPE) {
						event.consume();
						boolean confirm = MessageBox.confirm(getCurrentStage(), "Apakah anda akan membatalkan pemilihan salesperson?", "Konfirmasi");
						if (confirm) {
							selectedSalesperson = null;
							closeDialog();
						}
						return;
					}

					if (code == KeyCode.UP || code == KeyCode.DOWN) {
						if (!tableViewResult.isFocused()) {
							tableViewResult.requestFocus();
							if (tableViewResult.getSelectionModel().getSelectedItem() == null && !tableViewResult.getItems().isEmpty()) {
								tableViewResult.getSelectionModel().select(0);
							}
						}
						return;
					}

					if (code == KeyCode.ENTER) {
						event.consume();
						if (searchInput.isFocused()) {
							handleSearch();
						} else if (tableViewResult.isFocused()) {
							confirmSelection();
						}
						return;
					}

					if (!searchInput.isFocused()) {
						if (code.isNavigationKey() || code.isFunctionKey() || code.isModifierKey() || code == KeyCode.TAB) {
							return;
						}
						searchInput.requestFocus();
					}
				});
			}
		});

		// Auto-focus search input and load initial data
		Platform.runLater(() -> {
			searchInput.requestFocus();
			handleSearch();
		});
	}

	private void handleSearch() {
		String keyword = searchInput.getText() != null ? searchInput.getText().trim() : "";
		List<Salesperson> salespersons = salespersonRepository.search(keyword);
		ObservableList<Salesperson> data = FXCollections.observableArrayList(salespersons);
		tableViewResult.setItems(data);
		
		if (!data.isEmpty()) {
			tableViewResult.getSelectionModel().select(0);
			tableViewResult.requestFocus();
		} else {
			if (!keyword.isEmpty()) {
				MessageBox.info(getCurrentStage(), "No salesperson found.", "Search Results");
			}
		}
	}

	private void confirmSelection() {
		selectedSalesperson = tableViewResult.getSelectionModel().getSelectedItem();
		if (selectedSalesperson != null) {
			closeDialog();
		} else {
			MessageBox.info(getCurrentStage(), "Please select a salesperson from the list.", "Selection Warning");
		}
	}

	private void closeDialog() {
		if (submitButton.getScene() != null && submitButton.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	private Stage getCurrentStage() {
		if (submitButton.getScene() != null && submitButton.getScene().getWindow() instanceof Stage stage) {
			return stage;
		}
		return null;
	}

	public Salesperson getSelectedSalesperson() {
		return selectedSalesperson;
	}
}
