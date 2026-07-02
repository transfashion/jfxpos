package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfxpos.Controller;

public class CustDisplayController extends Controller {

	@FXML
	private Label totalLabel;

	public CustDisplayController() {
		super(CustDisplayController.class);
	}

	public void setTotal(String total) {
		if (totalLabel != null) {
			javafx.application.Platform.runLater(() -> totalLabel.setText(total));
		}
	}
}
