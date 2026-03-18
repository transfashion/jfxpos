package launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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
		splashStage.setWidth(400);
		splashStage.setHeight(250);
		splashStage.setScene(scene);
		splashStage.setAlwaysOnTop(true);
		splashStage.getIcons().add(
				new Image(getClass().getResourceAsStream("/bag.png")));
		splashStage.show();

		Platform.runLater(() -> {
			Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
			splashStage.setX((bounds.getWidth() - splashStage.getWidth()) / 2);
			splashStage.setY((bounds.getHeight() - splashStage.getHeight()) / 2);
		});

		// Background loading task
		new Thread(() -> {
			try {
				Platform.runLater(() -> status.setText("Loading JFXPOS App..."));
				// Thread.sleep(3000); // Simulate some process delay for visibility

				Updater.update(status);

				Path jarPath = getJarPath();

				URL[] urls = { jarPath.toUri().toURL() };

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

					Stage primaryStage = new Stage();

					try {
						Object appInstance = appClass.getDeclaredConstructor().newInstance();
						Method startMethod = appClass.getMethod("start", Stage.class);

						// 1. Jalankan aplikasi utama
						status.setText("Starting JFX Point of Sales ...");
						System.out.println("Starting Main Applicaton");
						startMethod.invoke(appInstance, primaryStage);

						primaryStage.getIcons().add(
								new Image(getClass().getResourceAsStream("/bag.png")));

						primaryStage.setAlwaysOnTop(true);
						primaryStage.setAlwaysOnTop(false);

						closeSplashScreen(root, splashStage, primaryStage);

					} catch (Exception e) {
						status.setText("Load failed: " + e.getMessage());

						splashStage.setAlwaysOnTop(false);
						if (primaryStage.isShowing()) {
							primaryStage.close();
						}

						Alert alert = createAlert(e);
						closeSplashScreen(root, splashStage, primaryStage);
						alert.showAndWait(); // MODAL

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				Platform.runLater(() -> {
					splashStage.setAlwaysOnTop(false);
					Alert alert = createAlert(e);
					alert.showAndWait();
					splashStage.close();
				});
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Alert createAlert(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Load Failed!");

		String errorMessage = e.getMessage();
		if (errorMessage == null) {
			alert.setContentText(e.toString());
		} else {
			alert.setContentText(e.getMessage());
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		TextArea textArea = new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);

		alert.getDialogPane().setExpandableContent(textArea);

		return alert;
	}

	public static void closeSplashScreen(VBox root, Stage splashStage, Stage primaryStage) {
		// Buat Efek Fade Out untuk Splash Screen
		javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(
				Duration.millis(1000), root);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);

		// 3. Setelah animasi selesai, baru tutup splashStage
		fadeOut.setOnFinished(event -> {
			splashStage.close();
			if (primaryStage.isShowing()) {
				primaryStage.setAlwaysOnTop(false);
				primaryStage.toFront(); // Angkat window utama ke depan
				primaryStage.requestFocus();
			}
		});

		// tutup splash screen
		PauseTransition delay = new PauseTransition(Duration.seconds(1));
		delay.setOnFinished(evt -> fadeOut.play());
		delay.play();
	}

	public static Path getJarPath() throws IOException {
		Path jarPath;

		Path basePath = Paths.get(System.getProperty("user.dir"));
		Path jfxposCfg = basePath.resolve("app/jfxpos.cfg");
		Path jfxposJar = basePath.resolve("app/jfxpos.jar");
		Path ideJar = Paths.get("build/libs/jfxpos.jar").toAbsolutePath();

		if (Files.exists(jfxposCfg)) {
			// baca dulu dari konfigurasi file
			String fileName = getJfxposFile(jfxposCfg);
			jarPath = basePath.resolve("app/" + fileName);
			if (!Files.exists(jarPath)) {
				throw new RuntimeException("Tidak menemukan '" + jarPath.toString() + "'!");
			}
		} else if (Files.exists(jfxposJar)) {
			jarPath = jfxposJar;
		} else if (Files.exists(ideJar)) {
			jarPath = ideJar;
		} else {
			throw new RuntimeException("Tidak menemukan 'jfxpos.jar'!  basePath: " + basePath);
		}

		return jarPath;
	}

	public static String getJfxposFile(Path cfgFilePath) throws IOException {
		Properties props = new Properties();

		try (BufferedReader reader = new BufferedReader(new FileReader(cfgFilePath.toFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				// skip empty line atau komentar
				if (line.isEmpty() || line.startsWith("#") || line.startsWith(";") || line.startsWith("["))
					continue;

				// split key=value
				int idx = line.indexOf('=');
				if (idx > 0) {
					String key = line.substring(0, idx).trim();
					String value = line.substring(idx + 1).trim();
					props.setProperty(key, value);
				}
			}
		}

		// ambil nilai jfxpos
		String jfxpos = props.getProperty("jfxpos", "jfxpos.jar");
		return jfxpos;
	}
}
