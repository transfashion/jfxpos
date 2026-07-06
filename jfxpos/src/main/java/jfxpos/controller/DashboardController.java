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
	private Button signInButton;

	@FXML
	private Button updateJarButton;

	@FXML
	private Button sendDataButton;

	@FXML
	private Button signOffButton;

	@FXML
	private javafx.scene.layout.VBox progresInfoVBox;

	@FXML
	private javafx.scene.layout.VBox alertInfoVBox;

	@FXML
	private javafx.scene.control.ProgressBar progressBar;

	@FXML
	private javafx.scene.control.Label progressInfoLabel;

	@FXML
	private javafx.scene.control.Label progressTitleLabel;

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

	private static final int progressCompletedHideDelay = 5;

	public DashboardController(DashboardWindow window) {
		super(DashboardController.class);
		this.window = window;
	}

	@FXML
	public void initialize() {

		// sembunyikan progress sebelum ada proses yang berjalan
		if (progresInfoVBox != null) {
			progresInfoVBox.setVisible(false);
			progresInfoVBox.setManaged(false);
		}

		// sembuyikan alert info
		if (alertInfoVBox != null) {
			alertInfoVBox.setVisible(false);
			alertInfoVBox.setManaged(false);
		}

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

	private javafx.animation.PauseTransition hideDelay;

	private void setButtonsDisable(boolean disable) {
		if (signInButton != null)
			signInButton.setDisable(disable);
		if (updateDataButton != null)
			updateDataButton.setDisable(disable);
		if (updateJarButton != null)
			updateJarButton.setDisable(disable);
		if (sendDataButton != null)
			sendDataButton.setDisable(disable);
		if (signOffButton != null)
			signOffButton.setDisable(disable);
	}

	private void startProgress(String title, String initialMessage) {
		if (hideDelay != null) {
			hideDelay.stop();
			hideDelay = null;
		}
		setButtonsDisable(true);
		if (progresInfoVBox != null) {
			progresInfoVBox.setVisible(true);
			progresInfoVBox.setManaged(true);
		}
		updateProgress(0.0, title, initialMessage);
	}

	private void endProgress(String title, String finalMessage) {
		endProgress(title, finalMessage, false);
	}

	private void endProgress(String title, String finalMessage, boolean noHideDelay) {
		updateProgress(1.0, title, finalMessage);
		setButtonsDisable(false);
		if (noHideDelay) {
			if (hideDelay != null) {
				hideDelay.stop();
				hideDelay = null;
			}
			if (progresInfoVBox != null) {
				progresInfoVBox.setVisible(false);
				progresInfoVBox.setManaged(false);
			}
		} else {
			if (hideDelay != null) {
				hideDelay.stop();
			}
			hideDelay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(progressCompletedHideDelay));
			hideDelay.setOnFinished(event -> {
				if (progresInfoVBox != null) {
					progresInfoVBox.setVisible(false);
					progresInfoVBox.setManaged(false);
				}
				hideDelay = null;
			});
			hideDelay.play();
		}
	}

	@FXML
	private void onUpdateDataClick() {
		if (jfxpos.App.config == null) {
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.error(stage, "Configuration is not loaded!", "Update Data");
			return;
		}

		if (updateDataButton.getScene() != null) {
			updateDataButton.getScene().setCursor(javafx.scene.Cursor.WAIT);
		}

		startProgress("Update Data", "Memulai update data...");

		Task<Void> syncTask = new Task<>() {
			@Override
			protected Void call() throws Exception {
				ItemSyncer syncer = new ItemSyncer();
				syncer.syncItem(jfxpos.App.config, (progress, title, message) -> {
					window.updateProgress(progress, title, message);
				});
				return null;
			}
		};

		syncTask.setOnSucceeded(e -> {
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			endProgress("Update Data", "Update data berhasil!", true);
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.info(stage, "Update data berhasil!", "Update Data");
		});

		syncTask.setOnFailed(e -> {
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Throwable ex = syncTask.getException();
			endProgress("Update Data", "Gagal update data: " + (ex != null ? ex.getMessage() : "Unknown error"));
			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.error(stage, "Gagal update data. Detail: " + (ex != null ? ex.getMessage() : "Unknown error"),
					ex, "Update Data");
		});

		new Thread(syncTask).start();
	}

	@FXML
	private void onSignInClick() {
		runDummyProcess("Sign In", "Proses Sign In sedang berlangsung...", "Sign In berhasil!");
	}

	@FXML
	private void onUpdateJarClick() {
		runDummyProcess("Update Jar", "Mengunduh pembaruan aplikasi (update jar)...", "Aplikasi berhasil diperbarui!");
	}

	@FXML
	private void onSendDataClick() {
		runDummyProcess("Send Data", "Mengirimkan data transaksi ke server...", "Data berhasil dikirim!");
	}

	@FXML
	private void onSignOffClick() {
		runDummyProcess("Sign Off", "Proses Sign Off sedang berlangsung...", "Sign Off berhasil!");
	}

	private void runDummyProcess(String title, String initialMsg, String successMsg) {
		startProgress(title, initialMsg);

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() throws Exception {
				for (int i = 1; i <= 10; i++) {
					Thread.sleep(200);
					final double progress = i / 10.0;
					final String msg = initialMsg + " (" + (i * 10) + "%)";
					javafx.application.Platform.runLater(() -> window.updateProgress(progress, title, msg));
				}
				return null;
			}
		};

		task.setOnSucceeded(e -> endProgress(title, successMsg));
		task.setOnFailed(e -> endProgress(title, "Gagal melakukan " + title));

		new Thread(task).start();
	}

	public void updateProgress(double progress, String title, String message) {
		if (progressBar != null) {
			progressBar.setProgress(progress);
		}
		if (progressTitleLabel != null) {
			progressTitleLabel.setText(title);
		}
		if (progressInfoLabel != null) {
			progressInfoLabel.setText(message);
		}
	}

	public void updateProgress(double progress, String message) {
		updateProgress(progress, "Progress", message);
	}
}
