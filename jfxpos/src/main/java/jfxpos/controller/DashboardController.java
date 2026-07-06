package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.views.AuthorizeDialog;

import javafx.concurrent.Task;
import jfxpos.util.MessageBox;
import jfxpossyn.sync.ItemSyncer;
import jfxpos.views.DashboardWindow;

public class DashboardController extends Controller {

	@FXML
	private Button updateDataButton;

	@FXML
	private javafx.scene.control.ProgressBar progressBar;

	@FXML
	private javafx.scene.control.Label progressLabel;

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

	private final DashboardWindow window;

	public DashboardController(DashboardWindow window) {
		super(DashboardController.class);
		this.window = window;
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

	@FXML
	private void onUpdateDataClick() {
		if (jfxpos.App.config == null) {
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.error(stage, "Configuration is not loaded!", "Update Data");
			return;
		}

		updateDataButton.setDisable(true);
		if (updateDataButton.getScene() != null) {
			updateDataButton.getScene().setCursor(javafx.scene.Cursor.WAIT);
		}

		Task<Void> syncTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
				ItemSyncer syncer = new ItemSyncer();
				syncer.syncItem(jfxpos.App.config, (progress, message) -> {
					window.updateProgress(progress, message);
				});
				return null;
			}
		};

		syncTask.setOnSucceeded(e -> {
			updateDataButton.setDisable(false);
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.info(stage, "Update data berhasil!", "Update Data");
		});

		syncTask.setOnFailed(e -> {
			updateDataButton.setDisable(false);
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			Throwable ex = syncTask.getException();
			MessageBox.error(stage, "Gagal update data. Detail: " + ex.getMessage(), ex, "Update Data");
		});

		new Thread(syncTask).start();
	}

	public void updateProgress(double progress, String message) {
		if (progressBar != null) {
			progressBar.setProgress(progress);
		}
		if (progressLabel != null) {
			progressLabel.setText(message);
		}
	}
}
