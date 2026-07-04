package jfxpos.controller;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;

public class SaleInputHandler {

    public static void requestFocus(TextField lineInput) {
        if (lineInput != null) {
            Platform.runLater(() -> lineInput.requestFocus());
        }
    }

    public static boolean isLineInputFocused(TextField lineInput) {
        return lineInput != null && lineInput.isFocused();
    }

    public static boolean isItemTableFocused(TableView<?> itemTable) {
        return itemTable != null && itemTable.isFocused();
    }

    public static void focusItemTable(TableView<?> itemTable) {
        if (itemTable != null) {
            itemTable.requestFocus();
            if (itemTable.getSelectionModel().getSelectedIndex() < 0 && !itemTable.getItems().isEmpty()) {
                itemTable.getSelectionModel().select(0);
            }
        }
    }

    public static void appendLineInput(TextField lineInput, String text) {
        if (lineInput != null) {
            lineInput.appendText(text);
            lineInput.positionCaret(lineInput.getText().length());
        }
    }

    public static void handleLeftArrow(TextField lineInput) {
        if (lineInput != null) {
            lineInput.requestFocus();
            Platform.runLater(() -> {
                int pos = lineInput.getCaretPosition();
                if (pos > 0) {
                    lineInput.positionCaret(pos - 1);
                }
            });
        }
    }

    public static void handleRightArrow(TextField lineInput) {
        if (lineInput != null) {
            lineInput.requestFocus();
            Platform.runLater(() -> {
                int pos = lineInput.getCaretPosition();
                if (pos < lineInput.getText().length()) {
                    lineInput.positionCaret(pos + 1);
                }
            });
        }
    }

    public static void handleBackspace(TextField lineInput) {
        if (lineInput != null) {
            lineInput.requestFocus();
            Platform.runLater(() -> {
                int pos = lineInput.getCaretPosition();
                String text = lineInput.getText();
                if (pos > 0 && text != null && !text.isEmpty()) {
                    lineInput.deleteText(pos - 1, pos);
                }
            });
        }
    }
}
