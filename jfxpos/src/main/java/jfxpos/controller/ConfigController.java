package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;

public class ConfigController {

	Stage stage;

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

	public void setStage(Stage stage) {
		this.stage = stage;
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
	private void onParseToken() {

	}

	@FXML
	private void onCancel() {

	}

	@FXML
	private void onSaveSetting() {

	}
}
