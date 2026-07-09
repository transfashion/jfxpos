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
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.models.Customer;
import jfxpos.util.MessageBox;
import jfxpos.util.JfxposApi;
import jfxpos.views.CustRegisterDialog;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustSearchController extends Controller {

	private static final Logger logger = Logger.getLogger(CustSearchController.class.getName());

	@FXML
	private Label customerTitleLabel;

	@FXML
	private TextField searchInput;

	@FXML
	private Button submitButton;

	@FXML
	private RadioButton radioById;

	@FXML
	private RadioButton radioByName;

	@FXML
	private RadioButton radioBy4LastDigit;

	@FXML
	private TableView<Customer> tableViewResult;

	@FXML
	private TableColumn<Customer, Long> colId;

	@FXML
	private TableColumn<Customer, String> colName;

	@FXML
	private TableColumn<Customer, String> colType;

	@FXML
	private TableColumn<Customer, Integer> colGender;

	@FXML
	private TableColumn<Customer, LocalDate> colBirthdate;

	@FXML
	private Button selectButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button newCustomerButton;

	private final ToggleGroup searchGroup = new ToggleGroup();
	private Customer selectedCustomer = null;

	public CustSearchController() {
		super(CustSearchController.class);
	}

	@FXML
	public void initialize() {
		// Group radio buttons
		radioById.setToggleGroup(searchGroup);
		radioByName.setToggleGroup(searchGroup);
		radioBy4LastDigit.setToggleGroup(searchGroup);

		// Configure TableView columns
		colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
		colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		colType.setCellValueFactory(new PropertyValueFactory<>("customerTypeName"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("customerGender"));
		colBirthdate.setCellValueFactory(new PropertyValueFactory<>("customerBirthdate"));

		// Format gender column display
		colGender.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else if (item == 1) {
					setText("Pria");
				} else if (item == 2) {
					setText("Wanita");
				} else {
					setText("Tidak Diketahui");
				}
			}
		});

		// Event handlers
		submitButton.setOnAction(e -> handleSearch());
		selectButton.setOnAction(e -> confirmSelection());
		cancelButton.setOnAction(e -> closeDialog());
		newCustomerButton.setOnAction(e -> openCustRegister());

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

		// Prevent auto select-all when focused so key inputs do not overwrite existing text
		searchInput.selectionProperty().addListener((obs, oldVal, newVal) -> {
			if (searchInput.isFocused() && newVal != null && newVal.getLength() > 0) {
				if (newVal.getStart() == 0 && newVal.getEnd() == searchInput.getLength()) {
					searchInput.deselect();
					searchInput.end();
				}
			}
		});

		// Keyboard controls and shortcuts on scene
		submitButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
					KeyCode code = event.getCode();

					if (code == KeyCode.ESCAPE) {
						event.consume();
						boolean confirm = MessageBox.confirm(getCurrentStage(), "Apakah anda akan membatalkan pemilihan customer ?", "Konfirmasi");
						if (confirm) {
							selectedCustomer = null;
							closeDialog();
						}
						return;
					}

					if (code == KeyCode.F1) {
						event.consume();
						rotateRadioButtons();
						return;
					}

					if (code == KeyCode.F2) {
						event.consume();
						newCustomerButton.fire();
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

		// Auto-focus search input
		Platform.runLater(() -> searchInput.requestFocus());
	}

	private void rotateRadioButtons() {
		if (radioById.isSelected()) {
			radioByName.setSelected(true);
		} else if (radioByName.isSelected()) {
			radioBy4LastDigit.setSelected(true);
		} else {
			radioById.setSelected(true);
		}
	}

	private void handleSearch() {
		String keyword = searchInput.getText() != null ? searchInput.getText().trim() : "";
		if (keyword.isEmpty()) {
			MessageBox.error(getCurrentStage(), "Search keyword cannot be empty!", "Search Validation");
			return;
		}

		if (radioById.isSelected()) {
			if (keyword.startsWith("0")) {
				keyword = keyword.replaceFirst("^0+", "");
				searchInput.setText(keyword);
				searchInput.end();
			}
			if (keyword.length() < 9 || !keyword.matches("\\d+")) {
				MessageBox.error(getCurrentStage(), "Search by ID requires at least 9 digits!", "Search Validation");
				return;
			}
		} else if (radioBy4LastDigit.isSelected()) {
			if (keyword.length() != 4 || !keyword.matches("\\d+")) {
				MessageBox.error(getCurrentStage(), "Search by last 4 digits requires exactly 4 digits!", "Search Validation");
				return;
			}
		}

		// Perform API Search
		try {
			AppConfig config = AppConfigStore.load();
			String serverUrl = config.serverUrl();
			if (serverUrl == null || serverUrl.isEmpty()) {
				throw new Exception("Server URL is not configured!");
			}
			if (serverUrl.endsWith("/")) {
				serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
			}

			String apiUrl = "";
			if (radioById.isSelected()) {
				// Exact ID search
				apiUrl = serverUrl + "/api/customers/" + keyword;
			} else if (radioByName.isSelected()) {
				// Search by name
				apiUrl = serverUrl + "/api/customers/search?searchtext="
						+ java.net.URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8);
			} else if (radioBy4LastDigit.isSelected()) {
				// Search by 4 last digits
				apiUrl = serverUrl + "/api/customers/searchlastdigit?searchtext="
						+ java.net.URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8);
			}

			logger.info("Requesting Customer API: " + apiUrl);
			HttpResponse<String> response = JfxposApi.get(apiUrl);

			if (response.statusCode() == 200) {
				String body = response.body();
				List<Customer> customers = parseCustomers(body);
				ObservableList<Customer> data = FXCollections.observableArrayList(customers);
				tableViewResult.setItems(data);
				if (!data.isEmpty()) {
					tableViewResult.getSelectionModel().select(0);
					tableViewResult.requestFocus();
				} else {
					MessageBox.info(getCurrentStage(), "No customers found.", "Search Results");
				}
			} else {
				String body = response.body();
				String errorMsg = extractJsonString(body, "message");
				if (errorMsg == null || errorMsg.isEmpty()) {
					errorMsg = "HTTP Status " + response.statusCode();
				}
				MessageBox.error(getCurrentStage(), "API Error: " + errorMsg, "Search Failed");
			}

		} catch (Exception e) {
			logger.log(java.util.logging.Level.SEVERE, "Customer search failed", e);
			MessageBox.error(getCurrentStage(), "Failed to search customer: " + e.getMessage(), "Search Error");
		}
	}

	private void confirmSelection() {
		selectedCustomer = tableViewResult.getSelectionModel().getSelectedItem();
		if (selectedCustomer != null) {
			closeDialog();
		} else {
			MessageBox.info(getCurrentStage(), "Please select a customer from the list.", "Selection Warning");
		}
	}

	private void closeDialog() {
		if (submitButton.getScene() != null && submitButton.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	private void openCustRegister() {
		try {
			CustRegisterDialog dialog = new CustRegisterDialog(getCurrentStage());
			String currentSearch = searchInput.getText() != null ? searchInput.getText().trim() : "";
			if (!currentSearch.isEmpty() && currentSearch.matches("\\d+")) {
				dialog.setCustomerId(currentSearch);
			}
			dialog.openDialog();
			if (dialog.isSaved()) {
				Customer c = new Customer();
				c.setCustomerId(Long.parseLong(dialog.getCustomerId()));
				c.setCustomerName(dialog.getCustomerName());
				String gender = dialog.getCustomerGender();
				c.setCustomerGender(gender != null && !gender.isEmpty() ? Integer.parseInt(gender) : 0);
				c.setCustomerBirthdate(dialog.getCustomerBirthdate());
				
				this.selectedCustomer = c;
				closeDialog();
			}
		} catch (Exception e) {
			logger.log(java.util.logging.Level.SEVERE, "Failed to open CustRegisterDialog", e);
		}
	}

	private Stage getCurrentStage() {
		if (submitButton.getScene() != null && submitButton.getScene().getWindow() instanceof Stage stage) {
			return stage;
		}
		return null;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	private List<Customer> parseCustomers(String json) {
		List<Customer> list = new ArrayList<>();
		if (json == null || json.isEmpty())
			return list;

		int dataIndex = json.indexOf("\"data\"");
		if (dataIndex == -1)
			return list;

		String dataPart = json.substring(dataIndex);
		int startBracket = dataPart.indexOf("[");
		int startBrace = dataPart.indexOf("{");

		if (startBracket != -1 && (startBrace == -1 || startBracket < startBrace)) {
			int endBracket = dataPart.lastIndexOf("]");
			if (endBracket == -1)
				return list;
			String arrayContent = dataPart.substring(startBracket + 1, endBracket);
			String[] objects = arrayContent.split("\\}(,[\\s]*)?\\{");
			for (int i = 0; i < objects.length; i++) {
				String objStr = objects[i];
				if (!objStr.startsWith("{"))
					objStr = "{" + objStr;
				if (!objStr.endsWith("}"))
					objStr = objStr + "}";
				Customer c = parseSingleCustomer(objStr);
				if (c != null) {
					list.add(c);
				}
			}
		} else if (startBrace != -1) {
			int endBrace = dataPart.lastIndexOf("}");
			if (endBrace != -1) {
				String objStr = dataPart.substring(startBrace, endBrace + 1);
				Customer c = parseSingleCustomer(objStr);
				if (c != null) {
					list.add(c);
				}
			}
		}

		return list;
	}

	private Customer parseSingleCustomer(String objJson) {
		String idStr = extractJsonString(objJson, "customer_id");
		if (idStr == null) {
			Integer idInt = extractJsonInt(objJson, "customer_id");
			if (idInt != null) {
				idStr = String.valueOf(idInt);
			}
		}
		if (idStr == null || idStr.isEmpty())
			return null;

		String name = extractJsonString(objJson, "customer_name");
		Integer typeId = extractJsonInt(objJson, "customertype_id");
		String typeName = extractJsonString(objJson, "customertype_name");
		Integer gender = extractJsonInt(objJson, "customer_gender");
		String birthdateStr = extractJsonString(objJson, "customer_birthdate");

		long customerId = 0L;
		try {
			customerId = Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			// Ignore
		}

		LocalDate birthdate = null;
		if (birthdateStr != null && birthdateStr.length() >= 10) {
			try {
				birthdate = LocalDate.parse(birthdateStr.substring(0, 10));
			} catch (Exception e) {
				// Ignore
			}
		}

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setCustomerName(name != null ? name : "");
		customer.setCustomerTypeId(typeId != null ? typeId : 0);
		customer.setCustomerTypeName(typeName != null ? typeName : "");
		customer.setCustomerGender(gender != null ? gender : 0);
		customer.setCustomerBirthdate(birthdate);

		return customer;
	}

	private String extractJsonString(String json, String key) {
		if (json == null)
			return null;
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"" + key + "\"[\\s]*:[\\s]*\"([^\"]*)\"");
		java.util.regex.Matcher matcher = pattern.matcher(json);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private Integer extractJsonInt(String json, String key) {
		if (json == null)
			return null;
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"" + key + "\"[\\s]*:[\\s]*([0-9]+)");
		java.util.regex.Matcher matcher = pattern.matcher(json);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return null;
	}
}
