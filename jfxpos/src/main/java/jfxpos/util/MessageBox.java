package jfxpos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

}
