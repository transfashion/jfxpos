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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
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
	private TableColumn<Customer, Integer> colId;

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

		// Double-click row to select
		tableViewResult.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Keyboard controls
		searchInput.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				handleSearch();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		tableViewResult.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		// Auto-focus search input
		Platform.runLater(() -> searchInput.requestFocus());
	}

	private void handleSearch() {
		String keyword = searchInput.getText() != null ? searchInput.getText().trim() : "";
		if (keyword.isEmpty()) {
			MessageBox.error(getCurrentStage(), "Search keyword cannot be empty!", "Search Validation");
			return;
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
				apiUrl = serverUrl + "/api/customers/search?searchtext=" + java.net.URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8);
			} else if (radioBy4LastDigit.isSelected()) {
				// Search by 4 last digits
				apiUrl = serverUrl + "/api/customers/searchlastdigit?searchtext=" + java.net.URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8);
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
		if (json == null || json.isEmpty()) return list;

		int dataIndex = json.indexOf("\"data\"");
		if (dataIndex == -1) return list;

		String dataPart = json.substring(dataIndex);
		int startBracket = dataPart.indexOf("[");
		int startBrace = dataPart.indexOf("{");

		if (startBracket != -1 && (startBrace == -1 || startBracket < startBrace)) {
			int endBracket = dataPart.lastIndexOf("]");
			if (endBracket == -1) return list;
			String arrayContent = dataPart.substring(startBracket + 1, endBracket);
			String[] objects = arrayContent.split("\\}(,[\\s]*)?\\{");
			for (int i = 0; i < objects.length; i++) {
				String objStr = objects[i];
				if (!objStr.startsWith("{")) objStr = "{" + objStr;
				if (!objStr.endsWith("}")) objStr = objStr + "}";
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
		if (idStr == null || idStr.isEmpty()) return null;

		String name = extractJsonString(objJson, "customer_name");
		Integer typeId = extractJsonInt(objJson, "customertype_id");
		String typeName = extractJsonString(objJson, "customertype_name");
		Integer gender = extractJsonInt(objJson, "customer_gender");
		String birthdateStr = extractJsonString(objJson, "customer_birthdate");

		int customerId = 0;
		try {
			customerId = Integer.parseInt(idStr);
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
