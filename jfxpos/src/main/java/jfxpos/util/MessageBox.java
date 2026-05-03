package jfxpos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MessageBox {

    // ini masih salah;
    static final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static void error(Stage stage, Exception ex) {
        error(stage, ex, "Error");
    }

    public static void error(Stage stage, Exception ex, String title) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setExpandableContent(textArea);

        error(stage, ex.getMessage(), title);
    }


    public static void error(Stage stage, String msg) {
        error(stage, msg, "Error");
    }

    public static void error(Stage stage, String msg, String title) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(msg);

        if (!(alert.getOwner() instanceof Stage)) {
            // ini salah!!!!, karena static staharusnya tidak disini
            alert.initOwner(stage);
            alert.initModality(Modality.WINDOW_MODAL);
        }


        alert.showAndWait();
    }

}
