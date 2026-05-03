package jfxpos.controller;

import javafx.fxml.FXML;
import jfxpos.Controller;

public class TesterController extends Controller {
	public TesterController() {
		super(TesterController.class);
	}

	@FXML
	private void onCheckForUpdateButtonClick() {
		logger.info("Check for Update...");
	}

}
