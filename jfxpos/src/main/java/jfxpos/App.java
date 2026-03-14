package jfxpos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		Label label = new Label("Simple POS Window");
		label.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

		StackPane root = new StackPane(label);
		root.setPrefSize(800, 600);

		Scene scene = new Scene(root);
		stage.setTitle("JFXPOS");
		stage.setScene(scene);
		stage.show();

		Platform.runLater(() -> {
			stage.setMaximized(true);
			stage.requestFocus();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void readConfiguration() {
		System.out.println("Reading configuration...");
		System.out.println("Configuration loaded!");

	}
}
