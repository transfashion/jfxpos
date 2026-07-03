package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.views.AuthorizeDialog;

public class DashboardController extends Controller {

	@FXML
	private javafx.scene.control.Label siteNameLabel;

	@FXML
	private javafx.scene.control.Label siteCodeLabel;

	@FXML
	private javafx.scene.control.Label structCodeLabel;

	@FXML
	private javafx.scene.control.Label deviceCodeLabel;

	@FXML
	private javafx.scene.control.Label nameLabel;

	@FXML
	private javafx.scene.control.Label serverUrlLabel;

	@FXML
	private Button voidButton;

	@FXML
	private Button posConsole1Button;

	@FXML
	private Button posConsole2Button;

	private jfxpos.views.SaleDialog saleDialog1;
	private jfxpos.views.SaleDialog saleDialog2;

	public DashboardController() {
		super(DashboardController.class);
	}

	@FXML
	public void initialize() {
		if (jfxpos.App.config != null) {
			siteNameLabel.setText(jfxpos.App.config.siteName());
			siteCodeLabel.setText(jfxpos.App.config.siteCode());
			structCodeLabel.setText(jfxpos.App.config.structCode());
			deviceCodeLabel.setText(jfxpos.App.config.deviceCode());
			nameLabel.setText(jfxpos.App.config.name());
			serverUrlLabel.setText(jfxpos.App.config.serverUrl());
		}
	}

	@FXML
	private void onVoidClick() {
		try {
			Stage owner = (Stage) voidButton.getScene().getWindow();
			AuthorizeDialog dlg = new AuthorizeDialog(owner);
			dlg.openDialog();
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}

	@FXML
	private void posConsoleButtonClick(javafx.event.ActionEvent event) {
		try {
			Stage owner = (Stage) voidButton.getScene().getWindow();
			if (event.getSource() == posConsole1Button) {
				if (saleDialog1 == null) {
					saleDialog1 = new jfxpos.views.SaleDialog(owner, 1);
				}
				saleDialog1.openDialog();
			} else if (event.getSource() == posConsole2Button) {
				if (saleDialog2 == null) {
					saleDialog2 = new jfxpos.views.SaleDialog(owner, 2);
				}
				saleDialog2.openDialog();
			}
		} catch (Exception ex) {
			ErrorMessage.show(ex);
		}
	}
}
