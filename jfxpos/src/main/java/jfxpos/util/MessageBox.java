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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        if (stage != null) {
            alert.initOwner(stage);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

        // Make responsive to keyboard: ENTER -> YES, ESC -> CANCEL
        Button yesBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        Button cancelBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (yesBtn != null) {
            yesBtn.setDefaultButton(true);
        }
        if (cancelBtn != null) {
            cancelBtn.setCancelButton(true);
        }

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

