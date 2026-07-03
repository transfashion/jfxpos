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
	TextField apiKeyInput;

	@FXML
	TextField siteCodeInput;

	@FXML
	TextField structCodeInput;

	@FXML
	TextField secretInput;

	@FXML
	TextField deviceCodeInput;

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

	private Integer apiDeviceId;
	private Integer apiSiteId;
	private String apiSiteName;
	private Integer apiStructId;
	private String apiName;

	public ConfigController() {
		super(ConfigController.class);
	}

	@FXML
	public void initialize() {
		initialConfig = AppConfigStore.load();
		serverUrlInput.setText(initialConfig.serverUrl());
		apiKeyInput.setText(initialConfig.apiKey());
		siteCodeInput.setText(initialConfig.siteCode());
		structCodeInput.setText(initialConfig.structCode());
		secretInput.setText(initialConfig.secret());
		deviceCodeInput.setText(initialConfig.deviceCode());
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
		if (!equalsOrEmpty(apiKeyInput.getText(), initialConfig.apiKey()))
			return true;
		if (!equalsOrEmpty(siteCodeInput.getText(), initialConfig.siteCode()))
			return true;
		if (!equalsOrEmpty(structCodeInput.getText(), initialConfig.structCode()))
			return true;
		if (!equalsOrEmpty(secretInput.getText(), initialConfig.secret()))
			return true;
		if (!equalsOrEmpty(deviceCodeInput.getText(), initialConfig.deviceCode()))
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
				// 1. Verify device credentials via API
				callGetDeviceApi();
				// 2. Test database connection
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
					apiKeyInput.getText(),
					siteCodeInput.getText(),
					apiSiteId != null ? apiSiteId : initialConfig.siteId(),
					apiSiteName != null ? apiSiteName : initialConfig.siteName(),
					structCodeInput.getText(),
					apiStructId != null ? apiStructId : initialConfig.structId(),
					deviceCodeInput.getText(),
					apiDeviceId != null ? apiDeviceId : initialConfig.deviceId(),
					apiName != null ? apiName : initialConfig.name(),
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
			logger.log(Level.SEVERE, "Configuration verification or connection check failed during Save", ex);

			// Show connection error message to user
			Stage stage = (Stage) saveButton.getScene().getWindow();
			String friendlyMsg;
			if (ex.getMessage() != null && (ex.getMessage().contains("API") || ex.getMessage().contains("perangkat")
					|| ex.getMessage().contains("Server URL"))) {
				friendlyMsg = ex.getMessage();
			} else {
				friendlyMsg = getFriendlyDatabaseErrorMessage(ex);
			}
			MessageBox.error(stage,
					"Gagal menyimpan konfigurasi.\n\n" + friendlyMsg, ex,
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

	private void callGetDeviceApi() throws Exception {
		String serverUrl = serverUrlInput.getText() != null ? serverUrlInput.getText().trim() : "";
		if (serverUrl.isEmpty()) {
			throw new Exception("Server URL tidak boleh kosong!");
		}
		if (serverUrl.endsWith("/")) {
			serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
		}
		String apiUrl = serverUrl + "/api/pos/getdevice";

		String deviceCode = deviceCodeInput.getText() != null ? deviceCodeInput.getText().trim() : "";
		String apiKey = apiKeyInput.getText() != null ? apiKeyInput.getText().trim() : "";
		String secret = secretInput.getText() != null ? secretInput.getText().trim() : "";
		String siteCode = siteCodeInput.getText() != null ? siteCodeInput.getText().trim() : "";
		String structCode = structCodeInput.getText() != null ? structCodeInput.getText().trim() : "";

		String timestamp = java.time.Instant.now().toString();
		String payload = "{}" + timestamp;

		String signature = calculateHmacSha256(payload, secret);

		java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
		java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
				.uri(java.net.URI.create(apiUrl))
				.GET()
				.header("X-Device-Code", deviceCode)
				.header("X-API-Key", apiKey)
				.header("X-Timestamp", timestamp)
				.header("X-Signature", signature)
				.header("X-Site-Code", siteCode)
				.header("X-Dept-Code", structCode)
				.build();

		java.net.http.HttpResponse<String> response = client.send(request,
				java.net.http.HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			String body = response.body();
			logger.info("Verifikasi Device Sukses: " + body);

			Integer deviceId = extractJsonInt(body, "device_id");
			String deviceName = extractJsonString(body, "name");
			Integer siteId = extractJsonInt(body, "site_id");
			String siteName = extractJsonString(body, "site_name");
			Integer structId = extractJsonInt(body, "struct_id");

			this.apiDeviceId = deviceId;
			this.apiName = deviceName;
			this.apiSiteId = siteId;
			this.apiSiteName = siteName;
			this.apiStructId = structId;
		} else {
			logger.warning(
					"Gagal Verifikasi Device. Status: " + response.statusCode() + ", Response: " + response.body());
			String errorMsg = extractJsonString(response.body(), "message");
			if (errorMsg == null || errorMsg.isEmpty()) {
				errorMsg = "HTTP Status " + response.statusCode();
			}
			throw new Exception("Gagal verifikasi perangkat (API Get Device): " + errorMsg);
		}
	}

	private String calculateHmacSha256(String data, String key) throws Exception {
		byte[] byteKey = key.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		javax.crypto.Mac sha256HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
		javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(byteKey, "HmacSHA256");
		sha256HMAC.init(keySpec);

		byte[] macData = sha256HMAC.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));

		StringBuilder result = new StringBuilder();
		for (byte b : macData) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
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
