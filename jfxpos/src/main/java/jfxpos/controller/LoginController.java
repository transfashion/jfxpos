package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import jfxpos.util.ErrorMessage;
import jfxpos.util.WindowManager;

public class LoginController {

	Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void onLogin() {
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

}
