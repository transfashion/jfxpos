package jfxpos.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.util.WindowManager;

public class LoginController extends Controller {

	@FXML
	private Label txtVersion;

	@FXML
	private Label txtNamedVersion;

	public LoginController() {
		super(LoginController.class);
	}

	@FXML
	private void onLogin() {
		logger.info("Login");

		try {

			// sementara, anggap berhasil
			boolean loginSucces = true;
			if (loginSucces) {
				WindowManager.openDashboard();
			}

		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}

	}

	@FXML
	private void onConfig() {
		try {
			WindowManager.openConfig();
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}

	@FXML
	public void initialize() {
		Properties p = new Properties();

		try (InputStream is = getClass().getResourceAsStream("/app.properties")) {
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String version = p.getProperty("app.version");
		String namedversion = p.getProperty("app.namedversion");

		if (version == null) {
			txtVersion.setText("DEV");
		} else {
			txtVersion.setText("v" + version);
		}

		if (namedversion == null) {
			txtNamedVersion.setText("");
		} else {
			txtNamedVersion.setText("- " + namedversion);
		}

	}
}
