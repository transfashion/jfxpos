package jfxpos.controller;

import javafx.fxml.FXML;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.util.WindowManager;

public class DashboardController extends Controller {

	public DashboardController() {
		super(DashboardController.class);
	}

	@FXML
	private void onTesterClick() {
		logger.info("Tester Clicked");
		try {
			WindowManager.openTesterWindow(this.stage);
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}
}
