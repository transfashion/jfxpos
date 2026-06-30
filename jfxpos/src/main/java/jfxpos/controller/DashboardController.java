package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.views.AuthorizeDialog;

public class DashboardController extends Controller {

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
