package jfxpos.controller;

import javafx.fxml.FXML;
import jfxpos.Controller;

public class DashboardController extends Controller {

	public DashboardController() {
		super(DashboardController.class);
	}

	@FXML
	private void onTesterClick() {
		logger.info("Tester Clicked");

	}
}
