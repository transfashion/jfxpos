package launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public final class Launcher extends Application {

	private static URLClassLoader appClassLoader;

	@Override
	public final void start(Stage splashStage) {
		// Simple Splash Screen UI
		Label title = new Label("JFX Point of Sales");
		title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
		Label status = new Label("Loading classes...");
		status.setStyle("-fx-font-size: 14px; -fx-text-fill: #ccc;");

		VBox root = new VBox(20, title, status);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: #2c3e50; -fx-padding: 40px; -fx-background-radius: 10;");

		Scene scene = new Scene(root, 400, 250);
		scene.setFill(null); // Transparent scene background

		splashStage.initStyle(StageStyle.TRANSPARENT);
		splashStage.setScene(scene);
		splashStage.setAlwaysOnTop(true);
		splashStage.show();

		// Background loading task
		new Thread(() -> {
			try {
				Platform.runLater(() -> status.setText("Loading JFXPOS App..."));
				// Thread.sleep(3000); // Simulate some process delay for visibility

				Updater.update(status);

				// Find jfxpos.jar
				File jarFile = new File("build/libs/jfxpos.jar");
				if (!jarFile.exists()) {
					Platform.runLater(() -> {
						status.setText("Error: jfxpos.jar not found!");
						status.setStyle("-fx-text-fill: #e74c3c;");
					});
					return;
				}

				URL[] urls = { jarFile.toURI().toURL() };

				// Use current class loader as parent to ensure JavaFX classes are found
				appClassLoader = new URLClassLoader(urls, Launcher.class.getClassLoader());
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					try {
						if (appClassLoader != null) {
							System.out.println("Closing appClassLoader...");
							appClassLoader.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}));

				Class<?> appClass = appClassLoader.loadClass("jfxpos.App");

				// Panggil metode static readConfiguration via refleksi
				Platform.runLater(() -> status.setText("Reading configuration files..."));
				Method configMethod = appClass.getMethod("readConfiguration");
				configMethod.invoke(null); // 'null' karena metodenya static

				// // Since App is also a JavaFX Application, we'll instantiate it
				// // and call its start method on a new Stage
				Platform.runLater(() -> {

					try {
						Object appInstance = appClass.getDeclaredConstructor().newInstance();
						Method startMethod = appClass.getMethod("start", Stage.class);

						Stage primaryStage = new Stage();

						// 1. Jalankan aplikasi utama
						status.setText("Starting JFX Point of Sales ...");
						System.out.println("Starting Main Applicaton");
						startMethod.invoke(appInstance, primaryStage);

						// Pastikan window utama muncul di belakang splash sebentar
						primaryStage.toBack();

						// 2. Buat Efek Fade Out untuk Splash Screen
						javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(
								Duration.millis(1000), root);
						fadeOut.setFromValue(1.0);
						fadeOut.setToValue(0.0);

						// 3. Setelah animasi selesai, baru tutup splashStage
						fadeOut.setOnFinished(event -> {
							splashStage.close();
							primaryStage.toFront(); // Angkat window utama ke depan
						});

						// Jalankan animasi setelah delay singkat (opsional)
						PauseTransition delay = new PauseTransition(Duration.seconds(1));
						delay.setOnFinished(e -> fadeOut.play());
						delay.play();

					} catch (Exception e) {
						e.printStackTrace();
						status.setText("Load failed: " + e.getMessage());
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				Platform.runLater(() -> status.setText("Load failed: " + e.getMessage()));
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
