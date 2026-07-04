package jfxpos.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.MessageBox;

import java.time.LocalDate;

public class CustRegisterController extends Controller {

	@FXML
	private TextField txtCustomerId;

	@FXML
	private TextField txtCustomerName;

	@FXML
	private RadioButton rbPria;

	@FXML
	private RadioButton rbWanita;

	private ToggleGroup genderGroup;

	@FXML
	private DatePicker dpBirthdate;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	private boolean saved = false;
	private String customerId = "";
	private String customerName = "";
	private String customerGender = "";
	private LocalDate customerBirthdate = null;

	public CustRegisterController() {
		super(CustRegisterController.class);
	}

	@FXML
	public void initialize() {
		// Initialize ToggleGroup programmatically to avoid SceneBuilder FXML loading errors
		genderGroup = new ToggleGroup();
		rbPria.setToggleGroup(genderGroup);
		rbWanita.setToggleGroup(genderGroup);

		// Event handler for Save button
		btnSave.setOnAction(e -> handleSave());

		// Event handler for Cancel button
		btnCancel.setOnAction(e -> handleCancel());

		// Force Customer Name to be uppercase
		txtCustomerName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.equals(newValue.toUpperCase())) {
				int caret = txtCustomerName.getCaretPosition();
				txtCustomerName.setText(newValue.toUpperCase());
				txtCustomerName.positionCaret(caret);
			}
		});

		// Setup keyboard shortcuts and enter key navigation
		txtCustomerId.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				String id = txtCustomerId.getText() != null ? txtCustomerId.getText().trim() : "";
				id = cleanCustomerId(id);
				Stage currentStage = (btnSave.getScene() != null) ? (Stage) btnSave.getScene().getWindow() : null;

				if (!validateCustomerId(id, currentStage)) {
					return;
				}

				txtCustomerName.requestFocus();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		txtCustomerName.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				String name = txtCustomerName.getText() != null ? txtCustomerName.getText().trim() : "";
				Stage currentStage = (btnSave.getScene() != null) ? (Stage) btnSave.getScene().getWindow() : null;

				if (name.isEmpty()) {
					MessageBox.error(currentStage, "Customer Name cannot be empty!", "Validation Error");
					txtCustomerName.requestFocus();
					return;
				}

				// Focus on selected radio button or default to rbPria
				if (rbWanita.isSelected()) {
					rbWanita.requestFocus();
				} else {
					rbPria.setSelected(true);
					rbPria.requestFocus();
				}
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		// Key handler on rbPria
		rbPria.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				rbPria.setSelected(true);
				dpBirthdate.requestFocus();
				event.consume();
			} else if (event.getCode() == KeyCode.TAB) {
				if (event.isShiftDown()) {
					txtCustomerName.requestFocus();
				} else {
					rbWanita.requestFocus();
				}
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		// Key handler on rbWanita
		rbWanita.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				rbWanita.setSelected(true);
				dpBirthdate.requestFocus();
				event.consume();
			} else if (event.getCode() == KeyCode.TAB) {
				if (event.isShiftDown()) {
					rbPria.requestFocus();
				} else {
					dpBirthdate.requestFocus();
				}
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		// Listen on both DatePicker and its editor for Enter key
		dpBirthdate.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				btnSave.requestFocus();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		if (dpBirthdate.getEditor() != null) {
			dpBirthdate.getEditor().setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					btnSave.requestFocus();
					event.consume();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					handleCancel();
					event.consume();
				}
			});
		}

		btnSave.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				handleSave();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		btnCancel.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				handleCancel();
				event.consume();
			}
		});

		// Auto focus Customer ID on load
		Platform.runLater(() -> txtCustomerId.requestFocus());
	}

	private void handleSave() {
		String id = txtCustomerId.getText() != null ? txtCustomerId.getText().trim() : "";
		id = cleanCustomerId(id);
		String name = txtCustomerName.getText() != null ? txtCustomerName.getText().trim() : "";

		String gender = "";
		if (rbPria.isSelected()) {
			gender = "1";
		} else if (rbWanita.isSelected()) {
			gender = "2";
		}

		LocalDate birthdate = dpBirthdate.getValue();

		Stage currentStage = null;
		if (btnSave.getScene() != null && btnSave.getScene().getWindow() instanceof Stage s) {
			currentStage = s;
		}

		if (!validateCustomerId(id, currentStage)) {
			return;
		}

		if (name.isEmpty()) {
			MessageBox.error(currentStage, "Customer Name cannot be empty!", "Validation Error");
			txtCustomerName.requestFocus();
			return;
		}

		if (gender.isEmpty()) {
			MessageBox.error(currentStage, "Please select Gender!", "Validation Error");
			rbPria.requestFocus();
			return;
		}

		this.customerId = id;
		this.customerName = name;
		this.customerGender = gender;
		this.customerBirthdate = birthdate;
		this.saved = true;

		closeDialog();
	}

	private void handleCancel() {
		this.saved = false;
		closeDialog();
	}

	private String cleanCustomerId(String id) {
		if (id != null && id.startsWith("0")) {
			String cleaned = id.replaceFirst("^0+", "");
			txtCustomerId.setText(cleaned);
			return cleaned;
		}
		return id;
	}

	private boolean validateCustomerId(String id, Stage currentStage) {
		if (id.isEmpty()) {
			MessageBox.error(currentStage, "Nomor HP tidak boleh kosong!", "Validation Error");
			txtCustomerId.requestFocus();
			return false;
		}
		if (!id.matches("^8[1-9]\\d{7,10}$")) {
			MessageBox.error(currentStage, "Nomor HP customer tidak valid", "Validation Error");
			txtCustomerId.requestFocus();
			return false;
		}
		return true;
	}

	private void closeDialog() {
		if (btnSave.getScene() != null && btnSave.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public boolean isSaved() {
		return saved;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public LocalDate getCustomerBirthdate() {
		return customerBirthdate;
	}

	public void setCustomerId(String id) {
		if (txtCustomerId != null) {
			txtCustomerId.setText(id);
		} else {
			this.customerId = id;
		}
	}

	public void setCustomerName(String name) {
		if (txtCustomerName != null) {
			txtCustomerName.setText(name);
		} else {
			this.customerName = name;
		}
	}

	public void setCustomerGender(String gender) {
		this.customerGender = gender;
		if (gender != null) {
			if (gender.equalsIgnoreCase("Pria") || gender.equals("1")) {
				if (rbPria != null)
					rbPria.setSelected(true);
			} else if (gender.equalsIgnoreCase("Wanita") || gender.equals("2")) {
				if (rbWanita != null)
					rbWanita.setSelected(true);
			}
		}
	}

	public void setCustomerBirthdate(LocalDate birthdate) {
		if (dpBirthdate != null) {
			dpBirthdate.setValue(birthdate);
		} else {
			this.customerBirthdate = birthdate;
		}
	}
}
