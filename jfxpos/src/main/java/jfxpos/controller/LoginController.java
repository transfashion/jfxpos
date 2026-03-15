package jfxpos.controller;

import javafx.fxml.FXML;
import jfxpos.WindowManager;

public class LoginController {
	@FXML
	private void onLogin() {
		System.out.println("Login button clicked");

		try {

			// anggap berhasil
			boolean loginSucces = true;
			if (loginSucces) {
				WindowManager.openDashboard();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@FXML
	private void onConfig() {
		System.out.println("Config button clicked");
	}

}
