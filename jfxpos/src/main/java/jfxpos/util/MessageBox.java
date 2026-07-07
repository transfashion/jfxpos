package jfxpos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Optional;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MessageBox {

	public static void error(Stage stage, Exception ex) {
		error(stage, ex, "Error");
	}

	public static void error(Stage stage, Exception ex, String title) {
		error(stage, ex.getMessage(), ex, title);
	}

	public static void error(Stage stage, String msg, Throwable ex, String title) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(msg);

		if (stage != null) {
			alert.initOwner(stage);
			alert.initModality(Modality.WINDOW_MODAL);
		}

		if (ex != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);

			TextArea textArea = new TextArea(sw.toString());
			textArea.setEditable(false);
			textArea.setWrapText(true);

			alert.getDialogPane().setExpandableContent(textArea);
		}

		alert.showAndWait();
	}

	public static void error(Stage stage, String msg) {
		error(stage, msg, "Error");
	}

	public static void error(Stage stage, String msg, String title) {
		error(stage, msg, null, title);
	}

	public static boolean confirm(Stage stage, String msg) {
		return confirm(stage, msg, "Konfirmasi");
	}

	public static boolean confirm(Stage stage, String msg, String title) {
		return confirm(stage, msg, title, ButtonType.YES);
	}

	public static boolean confirm(Stage stage, String msg, String title, ButtonType defaultButtonType) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);

		if (stage != null) {
			alert.initOwner(stage);
			alert.initModality(Modality.WINDOW_MODAL);
		}

		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

		// Make responsive to keyboard: ENTER -> fire focused button, ESC -> CANCEL
		Button yesBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		if (yesBtn != null) {
			yesBtn.setDefaultButton(false);
		}
		Button cancelBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
		if (cancelBtn != null) {
			cancelBtn.setDefaultButton(false);
			cancelBtn.setCancelButton(true);
		}

		if (defaultButtonType != null) {
			Button defaultBtn = (Button) alert.getDialogPane().lookupButton(defaultButtonType);
			if (defaultBtn != null) {
				alert.setOnShown(event -> javafx.application.Platform.runLater(defaultBtn::requestFocus));
			}
		}

		alert.getDialogPane().setOnKeyPressed(event -> {
			if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
				javafx.scene.Node focusOwner = alert.getDialogPane().getScene().getFocusOwner();
				if (focusOwner instanceof Button) {
					((Button) focusOwner).fire();
				}
			}
		});

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.YES;
	}

	public static void info(Stage stage, String msg) {
		info(stage, msg, "Informasi");
	}

	public static void info(Stage stage, String msg, String title) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);

		if (stage != null) {
			alert.initOwner(stage);
			alert.initModality(Modality.WINDOW_MODAL);
		}

		alert.showAndWait();
	}
}
