package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.models.Trx;

public class CheckoutController extends Controller {
	@FXML
	private HBox rootBox;

	private final Trx trx;

	public CheckoutController(Trx trx) {
		super(CheckoutController.class);
		this.trx = trx;
	}

	@FXML
	public void initialize() {
		if (rootBox != null) {
			rootBox.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ESCAPE) {
					closeDialog();
					event.consume();
				}
			});
			javafx.application.Platform.runLater(() -> rootBox.requestFocus());
		}
	}

	private void closeDialog() {
		if (rootBox != null && rootBox.getScene() != null && rootBox.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}
}
