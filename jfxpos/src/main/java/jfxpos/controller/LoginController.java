package jfxpos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.models.User;
import jfxpos.util.ErrorMessage;
import jfxpos.util.MessageBox;
import jfxpos.views.ConfigDialog;
import jfxpos.views.MainWindow;

public class LoginController extends Controller {
	final MainWindow window;

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

		Platform.runLater(() -> usernameInput.requestFocus());
	}

	@FXML
	void onLoginButtonClick() {
		logger.info("Login");

		Stage stage = (Stage) loginButton.getScene().getWindow();
		loginButton.setDisable(true);
		usernameInput.setDisable(true);
		passwordInput.setDisable(true);

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
					loginButton.setDisable(false);
					usernameInput.setDisable(false);
					passwordInput.setDisable(false);
				}
			});

			new Thread(loginTask).start();
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}

	private Task<User> createLoginTask() {
		String username = usernameInput.getText();
		String password = passwordInput.getText();

		return new Task<>() {
			@Override
			protected User call() throws Exception {
				logger.info("username: " + username);
				logger.info("password: " + password);

				// simulasi load
				Thread.sleep(2000);
				if (username.equals("agung") && password.equals("rahasia")) {
					logger.info("Login Successful");
					return new User();
				} else {
					logger.warning("Login not found!");
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
