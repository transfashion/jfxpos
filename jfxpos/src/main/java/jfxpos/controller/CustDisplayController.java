package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfxpos.Controller;

public class CustDisplayController extends Controller {

	@FXML
	private Label grandTotalValueLabel;

	public CustDisplayController() {
		super(CustDisplayController.class);
	}

	public void setGrandTotal(String grandTotal) {
		if (grandTotalValueLabel != null) {
			javafx.application.Platform.runLater(() -> grandTotalValueLabel.setText(grandTotal));
		}
	}
}
