package jfxpos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.config.AppConfigStore;
import jfxpos.models.User;
import jfxpos.repository.UserRepository;
import jfxpos.util.BCrypt;
import jfxpos.util.ErrorMessage;
import jfxpos.util.MessageBox;
import jfxpos.views.ConfigDialog;
import jfxpos.views.MainWindow;

public class LoginController extends Controller {
	final MainWindow window;
	private final UserRepository userRepository = new UserRepository();

	@FXML
	TextField usernameInput;

	@FXML
	PasswordField passwordInput;

	@FXML
	Label versionLabel;

	@FXML
	Label namedVersionLabel;

	@FXML
	Button loginButton;

	@FXML
	Label configButton;

	public LoginController(MainWindow window) {
		super(LoginController.class);
		this.window = window;
	}

	@FXML
	void initialize() {
		Properties p = new Properties();

		try (InputStream is = getClass().getResourceAsStream("/app.properties")) {
			p.load(is);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to load app.properties", e);
		}

		String version = p.getProperty("app.version");
		String namedversion = p.getProperty("app.namedversion");

		if (version == null) {
			versionLabel.setText("DEV");
		} else {
			versionLabel.setText("v" + version);
		}

		if (namedversion == null) {
			namedVersionLabel.setText("");
		} else {
			namedVersionLabel.setText("- " + namedversion);
		}

		String lastUser = AppConfigStore.getLastUsername();
		if (lastUser != null && !lastUser.isEmpty()) {
			usernameInput.setText(lastUser);
		}
		setupFocusTracker();

		usernameInput.setOnAction(e -> {
			String username = usernameInput.getText();
			if (username != null && !username.trim().isEmpty()) {
				passwordInput.requestFocus();
			}
		});

		passwordInput.setOnAction(e -> {
			String password = passwordInput.getText();
			if (password != null && !password.isEmpty()) {
				loginButton.requestFocus();
			}
		});

		loginButton.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				loginButton.fire();
			}
		});
	}

	private void setupFocusTracker() {
		usernameInput.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				if (newScene.getWindow() != null) {
					registerShowingListener(newScene.getWindow());
				} else {
					newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
						if (newWindow != null) {
							registerShowingListener(newWindow);
						}
					});
				}
			}
		});
	}

	private void registerShowingListener(javafx.stage.Window window) {
		Runnable requestFocusTask = () -> Platform.runLater(() -> usernameInput.requestFocus());
		if (window.isShowing()) {
			Platform.runLater(requestFocusTask);
		} else {
			window.showingProperty().addListener((o, oldShowing, newShowing) -> {
				if (newShowing) {
					Platform.runLater(requestFocusTask);
				}
			});
		}
	}

	@FXML
	void onLoginButtonClick() {
		logger.info("Login process started");

		Stage stage = (Stage) loginButton.getScene().getWindow();
		String username = usernameInput.getText();
		String password = passwordInput.getText();

		if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
			MessageBox.error(stage, "Username dan password tidak boleh kosong!", "Login");
			return;
		}

		loginButton.setDisable(true);
		usernameInput.setDisable(true);
		passwordInput.setDisable(true);
		if (loginButton.getScene() != null) {
			loginButton.getScene().setCursor(javafx.scene.Cursor.WAIT);
		}

		try {
			Task<User> loginTask = createLoginTask();
			loginTask.setOnSucceeded(e -> {
				try {
					User user = loginTask.getValue();
					if (user == null) {
						MessageBox.error(stage, "username atau password salah!", "Login");
						return;
					}

					window.setDashboardView();
				} catch (Exception ex) {
					MessageBox.error(stage, ex);
				} finally {
					reEnableInputs();
				}
			});

			loginTask.setOnFailed(e -> {
				Throwable ex = loginTask.getException();
				logger.log(Level.SEVERE, "Login task failed", ex);
				String errorMsg = ex.getMessage();
				if (errorMsg != null && errorMsg.contains("koneksi gagal, periksa configurasi database")) {
					MessageBox.error(stage, "koneksi gagal, periksa configurasi database", ex, "Login");
				} else {
					MessageBox.error(stage, "Gagal login. Detail: " + ex.getMessage(), ex, "Login");
				}
				reEnableInputs();
			});

			new Thread(loginTask).start();
		} catch (Exception ex) {
			MessageBox.error(stage, ex);
			reEnableInputs();
		}
	}

	private void reEnableInputs() {
		loginButton.setDisable(false);
		usernameInput.setDisable(false);
		passwordInput.setDisable(false);
		if (loginButton.getScene() != null) {
			loginButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
		}
	}

	private Task<User> createLoginTask() {
		String username = usernameInput.getText().trim();
		String password = passwordInput.getText();

		return new Task<>() {
			@Override
			protected User call() throws Exception {
				logger.info("Verifying credentials for username: " + username);

				// Initialize database connection pool on first login attempt if not already initialized
				if (!jfxpos.util.DbPool.isInitialized()) {
					logger.info("Initializing database connection pool on first login...");
					jfxpos.util.DbPool.init(jfxpos.App.config);
				}

				// Verify connection is working
				try (java.sql.Connection conn = jfxpos.util.DbPool.getConnection()) {
					logger.info("Database connection verified successfully.");
				} catch (java.sql.SQLException e) {
					logger.log(Level.SEVERE, "Database connection failed during login", e);
					throw new Exception("koneksi gagal, periksa configurasi database", e);
				}

				User user = userRepository.findByUsername(username);
				if (user == null) {
					logger.warning("Username not found in database: " + username);
					return null;
				}

				if (!user.isActive()) {
					logger.warning("User is inactive: " + username);
					throw new Exception("Akun pengguna tidak aktif.");
				}

				if (BCrypt.checkpw(password, user.getPassword())) {
					logger.info("BCrypt password check matched for user: " + username);
					AppConfigStore.saveLastUsername(username);
					return user;
				} else {
					logger.warning("Incorrect password for user: " + username);
					return null;
				}
			}
		};
	}

	@FXML
	void onConfigButtonClick() {
		try {
			Stage owner = (Stage) configButton.getScene().getWindow();
			ConfigDialog dlg = new ConfigDialog(owner);
			dlg.openDialog();
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}

}
