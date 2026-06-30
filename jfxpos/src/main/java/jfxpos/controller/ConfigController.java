package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.Controller;
import jfxpos.util.MessageBox;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import jfxpos.util.ErrorMessage;
import jfxpos.views.TesterDialog;

import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class ConfigController extends Controller {

	@FXML
	TextField tokenInput;

	@FXML
	Button parseButton;

	@FXML
	TextField serverUrlInput;

	@FXML
	TextField siteIdInput;

	@FXML
	TextField secretInput;

	@FXML
	TextField machineIdInput;

	@FXML
	TextField printerNameInput;

	@FXML
	Button printerTestButton;

	@FXML
	Button saveButton;

	@FXML
	Button cancelButton;

	@FXML
	Button testButton;

	@FXML
	TextField databaseHostInput;

	@FXML
	TextField databasePathInput;

	@FXML
	TextField databaseUsernameInput;

	@FXML
	TextField databasePasswordInput;

	@FXML
	TextField databaseRoleInput;

	@FXML
	TextField databasePoolSizeInput;

	@FXML
	Button databaseTestConnectButton;

	private AppConfig initialConfig;

	public ConfigController() {
		super(ConfigController.class);
	}

	@FXML
	public void initialize() {
		initialConfig = AppConfigStore.load();
		serverUrlInput.setText(initialConfig.serverUrl());
		siteIdInput.setText(initialConfig.siteId());
		secretInput.setText(initialConfig.secret());
		machineIdInput.setText(initialConfig.machineId());
		printerNameInput.setText(initialConfig.ticketPrinterName());
		databaseHostInput.setText(initialConfig.databaseHost());
		databasePathInput.setText(initialConfig.databasePath());
		databaseUsernameInput.setText(initialConfig.databaseUsername());
		databasePasswordInput.setText(initialConfig.databasePassword());
		databaseRoleInput.setText(initialConfig.databaseRole());
		databasePoolSizeInput.setText(String.valueOf(initialConfig.databasePoolSize()));

		printerTestButton.setOnAction(e -> onPrinterTestButtonClick());
		databaseTestConnectButton.setOnAction(e -> onDatabaseTestConnectButtonClick());

		setupCloseRequestFilter();
	}

	@FXML
	private void onParseButtonClick() {
		logger.info("Parse button clicked");
	}

	@FXML
	private void onCancelButtonClick() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		if (isModified()) {
			boolean confirm = showExitConfirmation(stage);
			if (confirm) {
				stage.close();
			}
		} else {
			stage.close();
		}
	}

	@FXML
	private void onTestButtonClick() {
		try {
			Stage owner = (Stage) testButton.getScene().getWindow();
			TesterDialog dlg = new TesterDialog(owner);
			dlg.openDialog();
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}

	private void setupCloseRequestFilter() {
		Runnable configureStage = () -> {
			if (cancelButton.getScene() != null && cancelButton.getScene().getWindow() instanceof Stage stage) {
				stage.setOnCloseRequest(event -> {
					if (isModified()) {
						boolean confirm = showExitConfirmation(stage);
						if (!confirm) {
							event.consume();
						}
					}
				});
			}
		};

		if (cancelButton.getScene() != null) {
			if (cancelButton.getScene().getWindow() != null) {
				configureStage.run();
			} else {
				cancelButton.getScene().windowProperty().addListener((obs, oldW, newW) -> {
					if (newW != null) {
						configureStage.run();
					}
				});
			}
		} else {
			cancelButton.sceneProperty().addListener((obs, oldS, newS) -> {
				if (newS != null) {
					if (newS.getWindow() != null) {
						configureStage.run();
					} else {
						newS.windowProperty().addListener((obsW, oldW, newW) -> {
							if (newW != null) {
								configureStage.run();
							}
						});
					}
				}
			});
		}
	}

	private boolean isModified() {
		if (initialConfig == null) {
			return false;
		}
		if (!equalsOrEmpty(serverUrlInput.getText(), initialConfig.serverUrl()))
			return true;
		if (!equalsOrEmpty(siteIdInput.getText(), initialConfig.siteId()))
			return true;
		if (!equalsOrEmpty(secretInput.getText(), initialConfig.secret()))
			return true;
		if (!equalsOrEmpty(machineIdInput.getText(), initialConfig.machineId()))
			return true;
		if (!equalsOrEmpty(printerNameInput.getText(), initialConfig.ticketPrinterName()))
			return true;
		if (!equalsOrEmpty(databaseHostInput.getText(), initialConfig.databaseHost()))
			return true;
		if (!equalsOrEmpty(databasePathInput.getText(), initialConfig.databasePath()))
			return true;
		if (!equalsOrEmpty(databaseUsernameInput.getText(), initialConfig.databaseUsername()))
			return true;
		if (!equalsOrEmpty(databasePasswordInput.getText(), initialConfig.databasePassword()))
			return true;
		if (!equalsOrEmpty(databaseRoleInput.getText(), initialConfig.databaseRole()))
			return true;

		String initialPoolSizeStr = String.valueOf(initialConfig.databasePoolSize());
		if (!equalsOrEmpty(databasePoolSizeInput.getText(), initialPoolSizeStr))
			return true;

		return false;
	}

	private boolean equalsOrEmpty(String fieldText, String configValue) {
		String f = fieldText == null ? "" : fieldText.trim();
		String c = configValue == null ? "" : configValue.trim();
		return f.equals(c);
	}

	private boolean showExitConfirmation(Stage stage) {
		return MessageBox.confirm(stage, "ada perubahan pada konfigurasi, apakah akan keluar", "Konfirmasi Keluar");
	}

	@FXML
	private void onSaveButtonClick() {
		logger.info("Save Setting");
		String host = databaseHostInput.getText() != null ? databaseHostInput.getText().trim() : "";
		String path = databasePathInput.getText() != null ? databasePathInput.getText().trim() : "";
		String username = databaseUsernameInput.getText() != null ? databaseUsernameInput.getText().trim() : "";
		String password = databasePasswordInput.getText() != null ? databasePasswordInput.getText().trim() : "";
		String role = databaseRoleInput.getText() != null ? databaseRoleInput.getText().trim() : "";

		if (host.isEmpty() || path.isEmpty()) {
			Stage stage = (Stage) saveButton.getScene().getWindow();
			MessageBox.error(stage, "Host dan Path database tidak boleh kosong!", "Test Connection");
			return;
		}

		// Disable UI inputs during check
		saveButton.setDisable(true);
		cancelButton.setDisable(true);
		databaseTestConnectButton.setDisable(true);
		if (saveButton.getScene() != null) {
			saveButton.getScene().setCursor(javafx.scene.Cursor.WAIT);
		}

		Task<Void> testTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
				testDatabaseConnection(host, path, username, password, role);
				return null;
			}
		};

		testTask.setOnSucceeded(e -> {
			// Restore UI state
			saveButton.setDisable(false);
			cancelButton.setDisable(false);
			databaseTestConnectButton.setDisable(false);
			if (saveButton.getScene() != null) {
				saveButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}

			// Save config and close
			int poolSize = 3;
			try {
				poolSize = Integer.parseInt(databasePoolSizeInput.getText().trim());
			} catch (NumberFormatException ex) {
				logger.warning("Invalid pool size format, using default: 3");
			}

			AppConfig cfg = new AppConfig(
					serverUrlInput.getText(),
					siteIdInput.getText(),
					machineIdInput.getText(),
					secretInput.getText(),
					printerNameInput.getText(),
					host,
					path,
					username,
					password,
					role,
					poolSize);
			AppConfigStore.save(cfg);
			jfxpos.App.config = cfg;
			jfxpos.util.DbPool.init(cfg);

			Stage stage = (Stage) saveButton.getScene().getWindow();
			stage.close();
		});

		testTask.setOnFailed(e -> {
			// Restore UI state
			saveButton.setDisable(false);
			cancelButton.setDisable(false);
			databaseTestConnectButton.setDisable(false);
			if (saveButton.getScene() != null) {
				saveButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}

			Throwable ex = testTask.getException();
			logger.log(Level.SEVERE, "Database connection check failed during Save", ex);

			// Show connection error message to user
			Stage stage = (Stage) saveButton.getScene().getWindow();
			String friendlyMsg = getFriendlyDatabaseErrorMessage(ex);
			MessageBox.error(stage,
					"Gagal menyimpan konfigurasi: Koneksi database tidak dapat dibangun.\n\n" + friendlyMsg, ex,
					"Save Config Failed");
		});

		new Thread(testTask).start();
	}

	private void onPrinterTestButtonClick() {
		logger.info("Test Print clicked. Printer: " + printerNameInput.getText());
	}

	private void onDatabaseTestConnectButtonClick() {
		String host = databaseHostInput.getText() != null ? databaseHostInput.getText().trim() : "";
		String path = databasePathInput.getText() != null ? databasePathInput.getText().trim() : "";
		String username = databaseUsernameInput.getText() != null ? databaseUsernameInput.getText().trim() : "";
		String password = databasePasswordInput.getText() != null ? databasePasswordInput.getText().trim() : "";
		String role = databaseRoleInput.getText() != null ? databaseRoleInput.getText().trim() : "";

		if (host.isEmpty() || path.isEmpty()) {
			Stage stage = (Stage) databaseTestConnectButton.getScene().getWindow();
			MessageBox.error(stage, "Host dan Path database tidak boleh kosong!", "Test Connection");
			return;
		}

		databaseTestConnectButton.setDisable(true);
		if (databaseTestConnectButton.getScene() != null) {
			databaseTestConnectButton.getScene().setCursor(javafx.scene.Cursor.WAIT);
		}

		Task<Void> testTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
				testDatabaseConnection(host, path, username, password, role);
				return null;
			}
		};

		testTask.setOnSucceeded(e -> {
			databaseTestConnectButton.setDisable(false);
			if (databaseTestConnectButton.getScene() != null) {
				databaseTestConnectButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Test Connection");
			alert.setHeaderText(null);
			alert.setContentText("Koneksi ke database berhasil!");
			alert.initOwner((Stage) databaseTestConnectButton.getScene().getWindow());
			alert.showAndWait();
		});

		testTask.setOnFailed(e -> {
			databaseTestConnectButton.setDisable(false);
			if (databaseTestConnectButton.getScene() != null) {
				databaseTestConnectButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Throwable ex = testTask.getException();
			logger.log(Level.SEVERE, "Database connection test failed", ex);

			// Show error dialog
			Stage stage = (Stage) databaseTestConnectButton.getScene().getWindow();
			String friendlyMsg = getFriendlyDatabaseErrorMessage(ex);
			MessageBox.error(stage, friendlyMsg, ex, "Test Connection Failed");
		});

		new Thread(testTask).start();
	}

	private void testDatabaseConnection(String host, String path, String username, String password, String role)
			throws Exception {
		String url;
		if (host.contains(":")) {
			url = "jdbc:firebirdsql://" + host + "/" + path;
		} else {
			url = "jdbc:firebirdsql://" + host + ":3050/" + path;
		}

		logger.info("Testing connection to: " + url);

		// Force driver registration
		Class.forName("org.firebirdsql.jdbc.FBDriver");

		Properties props = new Properties();
		props.setProperty("user", username);
		props.setProperty("password", password);
		if (!role.isEmpty()) {
			props.setProperty("roleName", role);
		}

		// Set login timeout to 5 seconds to avoid freezing for too long
		DriverManager.setLoginTimeout(5);

		try (Connection conn = DriverManager.getConnection(url, props)) {
			if (conn == null || conn.isClosed()) {
				throw new SQLException("Connection returned is null or closed");
			}
		}
	}

	private String getFriendlyDatabaseErrorMessage(Throwable ex) {
		if (ex == null) {
			return "Terjadi kesalahan tidak dikenal saat menguji koneksi.";
		}

		String message = ex.getMessage();
		if (message == null) {
			message = ex.toString();
		}

		String lowerMsg = message.toLowerCase();

		// Check for username/password error
		if (lowerMsg.contains("user name and password are not defined")
				|| lowerMsg.contains("username atau password")
				|| lowerMsg.contains("authentication failure")
				|| lowerMsg.contains("invalid authorization")
				|| lowerMsg.contains("credentials")
				|| lowerMsg.contains("protocoldescriptor")
				|| lowerMsg.contains("createwireoperations")
				|| lowerMsg.contains("335544472")) {
			return "Gagal masuk: Username atau password database salah.";
		}

		// Check for host/connection/network issues
		if (lowerMsg.contains("unable to complete network request")
				|| lowerMsg.contains("connection refused")
				|| lowerMsg.contains("host")
				|| lowerMsg.contains("unknownhost")
				|| lowerMsg.contains("socket")
				|| lowerMsg.contains("connect timed out")
				|| lowerMsg.contains("timeout")
				|| lowerMsg.contains("335544721")) {
			return "Gagal terhubung: Tidak dapat menjangkau server database.\n"
					+ "Silakan periksa Host/IP, pastikan server Firebird aktif di port 3050, dan terhubung ke jaringan.";
		}

		// Check for database file/path issues
		if (lowerMsg.contains("i/o error for file")
				|| lowerMsg.contains("failed to locate database")
				|| lowerMsg.contains("not a valid database")
				|| lowerMsg.contains("does not exist")
				|| lowerMsg.contains("database tidak ditemukan")
				|| lowerMsg.contains("335544344")) {
			return "Gagal memuat database: File database tidak ditemukan atau path database di server salah.";
		}

		// Check for driver errors
		if (ex instanceof ClassNotFoundException || lowerMsg.contains("driver")) {
			return "Driver database (Firebird JDBC) tidak ditemukan atau gagal dimuat.";
		}

		// Fallback to the original error message but cleaner
		return "Gagal terhubung ke database.\nDetail error: " + message;
	}
}
