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

	public ConfigController() {
		super(ConfigController.class);
	}

	@FXML
	public void initialize() {
		AppConfig cfg = AppConfigStore.load();
		serverUrlInput.setText(cfg.serverUrl());
		siteIdInput.setText(cfg.siteId());
		secretInput.setText(cfg.secret());
		machineIdInput.setText(cfg.machineId());
		printerNameInput.setText(cfg.ticketPrinterName());
		databaseHostInput.setText(cfg.databaseHost());
		databasePathInput.setText(cfg.databasePath());
		databaseUsernameInput.setText(cfg.databaseUsername());
		databasePasswordInput.setText(cfg.databasePassword());
		databaseRoleInput.setText(cfg.databaseRole());
		databasePoolSizeInput.setText(String.valueOf(cfg.databasePoolSize()));

		printerTestButton.setOnAction(e -> onPrinterTestButtonClick());
		databaseTestConnectButton.setOnAction(e -> onDatabaseTestConnectButtonClick());
	}

	@FXML
	private void onParseButtonClick() {
		logger.info("Parse button clicked");
	}

	@FXML
	private void onCancelButtonClick() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void onSaveButtonClick() {
		logger.info("Save Setting");
		int poolSize = 3;
		try {
			poolSize = Integer.parseInt(databasePoolSizeInput.getText().trim());
		} catch (NumberFormatException e) {
			logger.warning("Invalid pool size format, using default: 3");
		}

		AppConfig cfg = new AppConfig(
				serverUrlInput.getText(),
				siteIdInput.getText(),
				machineIdInput.getText(),
				secretInput.getText(),
				printerNameInput.getText(),
				databaseHostInput.getText(),
				databasePathInput.getText(),
				databaseUsernameInput.getText(),
				databasePasswordInput.getText(),
				databaseRoleInput.getText(),
				poolSize);
		AppConfigStore.save(cfg);

		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
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

		Task<Void> testTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
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
					if (conn != null && !conn.isClosed()) {
						logger.info("Connection test successful");
					} else {
						throw new SQLException("Connection returned is null or closed");
					}
				}
				return null;
			}
		};

		testTask.setOnSucceeded(e -> {
			databaseTestConnectButton.setDisable(false);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Test Connection");
			alert.setHeaderText(null);
			alert.setContentText("Koneksi ke database berhasil!");
			alert.initOwner((Stage) databaseTestConnectButton.getScene().getWindow());
			alert.showAndWait();
		});

		testTask.setOnFailed(e -> {
			databaseTestConnectButton.setDisable(false);
			Throwable ex = testTask.getException();
			logger.log(Level.SEVERE, "Database connection test failed", ex);

			// Show error dialog
			Stage stage = (Stage) databaseTestConnectButton.getScene().getWindow();
			String friendlyMsg = getFriendlyDatabaseErrorMessage(ex);
			MessageBox.error(stage, friendlyMsg, ex, "Test Connection Failed");
		});

		new Thread(testTask).start();
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
