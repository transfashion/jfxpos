package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.util.ErrorMessage;
import jfxpos.Controller;
import jfxpos.views.TesterDialog;

import java.util.logging.Level;

public class ConfigController extends Controller {

	@FXML
	TextField txtToken;

	@FXML
	TextField txtServerUrl;

	@FXML
	TextField txtSiteId;

	@FXML
	TextField txtKey;

	@FXML
	TextField txtMachineId;

	@FXML
	TextField txtTicketPrinter;

	@FXML
	CheckBox chkMaximizeMainWindow;

	@FXML
	Button testerButton;

	public ConfigController() {
		super(ConfigController.class);
	}

	@FXML
	public void initialize() {
		AppConfig cfg = AppConfigStore.load();
		txtServerUrl.setText(cfg.serverUrl());
		txtSiteId.setText(cfg.siteId());
		txtKey.setText(cfg.key());
		txtMachineId.setText(cfg.machineId());
		txtTicketPrinter.setText(cfg.ticketPrinterName());
	}

	@FXML
	private void onParseButtonClick() {

	}

	@FXML
	private void onCancelButtonClick() {

	}

	@FXML
	private void onSaveButtonClick() {
		logger.info("Save Setting");
	}

	@FXML
	private void onTesterButtonClick() {
		logger.info("Tester Clicked");
		try {
			Stage owner = (Stage)testerButton.getScene().getWindow();
			TesterDialog dlg = new TesterDialog(owner);
			dlg.openDialog();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			ErrorMessage.show(ex);
		}

	}
}
