package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.Controller;

public class ConfigController extends Controller {

	@FXML
	TextField tokenInput;

	@FXML
	Button parseButton;

	@FXML
	TextField serverUrlInput;

	@FXML
	TextField siteIdInput;

	@FXML
	TextField secretInput;

	@FXML
	TextField machineIdInput;

	@FXML
	TextField printerNameInput;

	@FXML
	Button printerTestButton;

	@FXML
	Button saveButton;

	@FXML
	Button cancelButton;

	@FXML
	TextField databaseHostInput;

	@FXML
	TextField databasePathInput;

	@FXML
	TextField databaseUsernameInput;

	@FXML
	TextField databasePasswordInput;

	@FXML
	TextField databaseRoleInput;

	@FXML
	TextField databasePoolSizeInput;

	@FXML
	Button databaseTestConnectButton;

	public ConfigController() {
		super(ConfigController.class);
	}

	@FXML
	public void initialize() {
		AppConfig cfg = AppConfigStore.load();
		serverUrlInput.setText(cfg.serverUrl());
		siteIdInput.setText(cfg.siteId());
		secretInput.setText(cfg.secret());
		machineIdInput.setText(cfg.machineId());
		printerNameInput.setText(cfg.ticketPrinterName());
		databaseHostInput.setText(cfg.databaseHost());
		databasePathInput.setText(cfg.databasePath());
		databaseUsernameInput.setText(cfg.databaseUsername());
		databasePasswordInput.setText(cfg.databasePassword());
		databaseRoleInput.setText(cfg.databaseRole());
		databasePoolSizeInput.setText(String.valueOf(cfg.databasePoolSize()));

		printerTestButton.setOnAction(e -> onPrinterTestButtonClick());
		databaseTestConnectButton.setOnAction(e -> onDatabaseTestConnectButtonClick());
	}

	@FXML
	private void onParseButtonClick() {
		logger.info("Parse button clicked");
	}

	@FXML
	private void onCancelButtonClick() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void onSaveButtonClick() {
		logger.info("Save Setting");
		int poolSize = 3;
		try {
			poolSize = Integer.parseInt(databasePoolSizeInput.getText().trim());
		} catch (NumberFormatException e) {
			logger.warning("Invalid pool size format, using default: 3");
		}

		AppConfig cfg = new AppConfig(
				serverUrlInput.getText(),
				siteIdInput.getText(),
				machineIdInput.getText(),
				secretInput.getText(),
				printerNameInput.getText(),
				databaseHostInput.getText(),
				databasePathInput.getText(),
				databaseUsernameInput.getText(),
				databasePasswordInput.getText(),
				databaseRoleInput.getText(),
				poolSize);
		AppConfigStore.save(cfg);

		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
	}

	private void onPrinterTestButtonClick() {
		logger.info("Test Print clicked. Printer: " + printerNameInput.getText());
	}

	private void onDatabaseTestConnectButtonClick() {
		logger.info("Database Test Connection clicked. Host: " + databaseHostInput.getText());
	}
}
