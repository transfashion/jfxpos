package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.util.ErrorMessage;
import jfxpos.views.AuthorizeDialog;

import javafx.concurrent.Task;
import jfxpos.util.MessageBox;
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
	private javafx.scene.control.Label alertTitleLabel;

	@FXML
	private javafx.scene.control.Label alertMessageLabel;

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
	private javafx.scene.control.Label posVersionLabel;

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

		java.util.Properties p = new java.util.Properties();
		try (java.io.InputStream is = getClass().getResourceAsStream("/app.properties")) {
			if (is != null) {
				p.load(is);
			}
		} catch (java.io.IOException e) {
			logger.log(java.util.logging.Level.WARNING, "Failed to load app.properties", e);
		}
		String version = p.getProperty("app.version");
		if (version == null) {
			version = getClass().getPackage().getImplementationVersion();
		}
		if (posVersionLabel != null) {
			if (version == null) {
				posVersionLabel.setText("Versi POS: DEV");
			} else {
				posVersionLabel.setText("Versi POS: v" + version);
			}
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
		if (alertInfoVBox != null) {
			alertInfoVBox.setVisible(false);
			alertInfoVBox.setManaged(false);
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

		Task<Boolean> syncTask = new Task<>() {
			@Override
			protected Boolean call() throws Exception {
				jfxpossyn.UpdateData updateData = new jfxpossyn.UpdateData();
				return updateData.start(jfxpos.App.config, (progress, title, message) -> {
					window.updateProgress(progress, title, message);
				});
			}
		};

		syncTask.setOnSucceeded(e -> {
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Boolean executed = syncTask.getValue();
			if (Boolean.TRUE.equals(executed)) {
				endProgress("Update Data", "Update data berhasil!", true);
				Stage stage = (Stage) updateDataButton.getScene().getWindow();
				MessageBox.info(stage, "Update data berhasil!", "Update Data");
			} else {
				endProgress("Update Data", "Data Up to date", true);
				Stage stage = (Stage) updateDataButton.getScene().getWindow();
				MessageBox.info(stage, "Data Up to date", "Update Data");
			}
		});

		syncTask.setOnFailed(e -> {
			if (updateDataButton.getScene() != null) {
				updateDataButton.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
			}
			Throwable ex = syncTask.getException();
			String errorMsg = (ex != null ? ex.getMessage() : "Unknown error");
			endProgress("Update Data", "Gagal update data: " + errorMsg);

			if (alertInfoVBox != null) {
				if (alertTitleLabel != null) {
					alertTitleLabel.setText("Gagal Update Data");
				}
				if (alertMessageLabel != null) {
					alertMessageLabel.setText(errorMsg);
				}
				alertInfoVBox.setVisible(true);
				alertInfoVBox.setManaged(true);
			}

			Stage stage = (Stage) updateDataButton.getScene().getWindow();
			MessageBox.error(stage, "Gagal update data. Detail: " + errorMsg,
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
