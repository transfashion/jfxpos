package jfxpos.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public final class ErrorMessage {

	public static void show(Exception ex) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Load Failed!");

		String errorMessage = ex.getMessage();
		if (errorMessage == null) {
			alert.setContentText(ex.toString());
		} else {
			alert.setContentText(ex.getMessage());
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);

		TextArea textArea = new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);

		alert.getDialogPane().setExpandableContent(textArea);

		alert.showAndWait(); // MODAL
	}

}
