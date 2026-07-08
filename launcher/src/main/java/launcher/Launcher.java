package launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher extends Application {
	public static final boolean isDev = !Objects.equals(System.getProperty("app.env", "prod"), "prod");

	private static final Logger logger = LaunchLogger.createLogger(Launcher.class.getName());
	private URLClassLoader appClassLoader;

	@Override
	public void start(Stage splashStage) {
		SplashView splash = createSplash(splashStage);

		Task<Class<?>> loadTask = createLoadTask();

		// bind status text → otomatis update (mirip state di JS)
		splash.status.textProperty().bind(loadTask.messageProperty());

		loadTask.setOnSucceeded(e -> {
			try {
				startMainApp(loadTask.getValue(), splashStage, splash.root);
			} catch (Exception ex) {
				showErrorAndExit(ex, splashStage);
			}
		});

		loadTask.setOnFailed(e -> showErrorAndExit(loadTask.getException(), splashStage));

		// String env = System.getProperty("app.env", "prod");
		// System.out.println(env);

		// start async (pakai virtual thread Java 21)
		Thread.startVirtualThread(loadTask);
	}

	// =========================
	// SPLASH UI
	// =========================
	private SplashView createSplash(Stage stage) {
		Label title = new Label(Config.TITLE);
		title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

		Label status = new Label("Initializing...");
		status.setStyle("-fx-font-size: 14px; -fx-text-fill: #ccc;");

		VBox root = new VBox(20, title, status);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: #2c3e50; -fx-padding: 40px; -fx-background-radius: 10;");

		Scene scene = new Scene(root, 400, 250);
		scene.setFill(null);

		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + Config.ICON))));

		centerStage(stage);
		stage.show();

		return new SplashView(root, status);
	}

	private void centerStage(Stage stage) {
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		stage.setX((bounds.getWidth() - 400) / 2);
		stage.setY((bounds.getHeight() - 250) / 2);
	}

	// =========================
	// BACKGROUND TASK (ASYNC FLOW)
	// =========================
	private Task<Class<?>> createLoadTask() {
		return new Task<>() {
			@Override
			protected Class<?> call() throws Exception {

				Path jarPath = getJarPath();
				logger.info("jarPath " + jarPath.toString());

				updateMessage("Loading updater...");
				Updater.update(jarPath, this::updateMessage);

				updateMessage("Preparing classloader...");
				java.util.List<URL> urlList = new java.util.ArrayList<>();
				urlList.add(jarPath.toUri().toURL());

				Path synJarPath = jarPath.getParent().resolve("jfxpossyn.jar");
				if (Files.exists(synJarPath)) {
					urlList.add(synJarPath.toUri().toURL());
					logger.info("Added jfxpossyn.jar to classloader from: " + synJarPath.toString());
				} else {
					Path ideSynJar = Paths.get("build", "libs", "jfxpossyn.jar").toAbsolutePath();
					if (Files.exists(ideSynJar)) {
						urlList.add(ideSynJar.toUri().toURL());
						logger.info("Added jfxpossyn.jar to classloader from IDE path: " + ideSynJar.toString());
					} else {
						logger.severe("not found: " + synJarPath);
						throw new RuntimeException("Tidak menemukan 'jfxpossyn.jar'!\r\n" + synJarPath);
					}
				}

				URL[] urls = urlList.toArray(new URL[0]);
				appClassLoader = new ChildFirstClassLoader(urls, Launcher.class.getClassLoader());
				registerShutdownHook();

				updateMessage("Loading application class...");
				String appname = Config.MODULE_NAME + ".App";
				Class<?> appClass = appClassLoader.loadClass(appname);

				updateMessage("Reading configuration...");
				Thread.sleep(300);
				Method configMethod = appClass.getMethod("readConfiguration");
				configMethod.invoke(null);

				updateMessage("Starting application...");
				Thread.sleep(300);
				return appClass;
			}
		};
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				if (appClassLoader != null) {
					appClassLoader.close();
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error closing app classloader", e);
			}
		}));
	}

	// =========================
	// START MAIN APP
	// =========================
	private void startMainApp(Class<?> appClass, Stage splashStage, VBox splashRoot) throws Exception {

		Stage primaryStage = new Stage();

		Object appInstance = appClass.getDeclaredConstructor().newInstance();
		Method startMethod = appClass.getMethod("start", Stage.class);

		startMethod.invoke(appInstance, primaryStage);

		primaryStage.getIcons()
				.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + Config.ICON))));

		primaryStage.toFront();
		primaryStage.requestFocus();

		closeSplash(splashRoot, splashStage, primaryStage);
	}

	// =========================
	// ERROR HANDLING
	// =========================
	private void showErrorAndExit(Throwable e, Stage splashStage) {
		logger.log(Level.SEVERE, "Error in main application", e);

		e.printStackTrace();

		splashStage.setAlwaysOnTop(false);

		Alert alert = createAlert(e);
		alert.showAndWait();

		Platform.exit();
	}

	// =========================
	// UTIL
	// =========================
	private static class SplashView {
		VBox root;
		Label status;

		SplashView(VBox root, Label status) {
			this.root = root;
			this.status = status;
		}
	}

	// Dummy placeholders (pakai implementasi kamu)
	private Path getJarPath() throws IOException {
		Path jarPath;

		Path basePath = Paths.get(System.getProperty("user.dir"));

		// Deteksi apakah dijalankan dari packaged app (jpackage/jlink)
		String javaHomeProp = System.getProperty("java.home");
		if (javaHomeProp != null) {
			Path javaHome = Paths.get(javaHomeProp);
			Path temp = javaHome;
			while (temp != null) {
				Path checkApp = temp.resolve("app");
				if (Files.exists(checkApp) && Files.isDirectory(checkApp)) {
					basePath = temp;
					logger.info("Found packaged 'app' directory relative to java.home at: " + basePath.toString());
					break;
				}
				temp = temp.getParent();
			}
		}

		logger.info("basePath: " + basePath.toString());

		Path fileCfg = basePath.resolve("app").resolve(Config.MODULE_NAME + ".cfg");
		Path fileJar = basePath.resolve("app").resolve(Config.MODULE_NAME + ".jar");
		Path ideFileJar = Paths.get("build", "libs", Config.MODULE_NAME + ".jar").toAbsolutePath();

		if (Files.exists(fileCfg)) {
			// baca dulu dari konfigurasi file
			String fileName = getFileJarFromCfg(fileCfg);
			jarPath = basePath.resolve("app").resolve(fileName);
			if (!Files.exists(jarPath)) {
				logger.severe("not found: " + jarPath);
				throw new RuntimeException("Tidak menemukan '" + jarPath + "'!\r\n" + jarPath);
			}
		} else if (Files.exists(fileJar)) {
			jarPath = fileJar;
		} else if (Files.exists(ideFileJar)) {
			jarPath = ideFileJar;
		} else {
			// file jar tidak ketemu, coba cari keatasnya
			Path parent = basePath.getParent();
			if (parent == null) {
				logger.severe("basePath parent is null, cannot resolve app");
				throw new RuntimeException("Cannot resolve app directory: parent of basePath is null");
			}
			jarPath = parent.resolve("app").resolve(Config.MODULE_NAME + ".jar");
			if (!Files.exists(jarPath)) {
				logger.severe("not found: " + jarPath);
				throw new RuntimeException("Tidak menemukan '" + Config.MODULE_NAME + ".jar'!\r\n" + jarPath);
			}

		}

		return jarPath;
	}

	public static String getFileJarFromCfg(Path cfgFilePath) throws IOException {
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

		return props.getProperty(Config.MODULE_NAME, Config.MODULE_NAME + ".jar");

	}

	private void closeSplash(VBox root, Stage splash, Stage main) {
		splash.close();
		main.show();
	}

	private Alert createAlert(Throwable e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText("Error");
		
		Throwable current = e;
		while (current.getCause() != null && (current.getMessage() == null || current instanceof java.lang.reflect.InvocationTargetException)) {
			current = current.getCause();
		}
		
		String message = current.getMessage();
		if (message == null || message.trim().isEmpty()) {
			message = current.toString();
		}
		
		alert.setContentText(message);
		return alert;
	}

	private static class ChildFirstClassLoader extends URLClassLoader {
		public ChildFirstClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return loadClass(name, false);
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			// First, check if the class has already been loaded
			Class<?> c = findLoadedClass(name);
			if (c == null) {
				// Avoid intercepting system/platform classes or JavaFX runtime classes
				if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("javafx.") || name.startsWith("jdk.")) {
					c = super.loadClass(name, resolve);
				} else {
					try {
						// Try to load from URLs first
						c = findClass(name);
					} catch (ClassNotFoundException e) {
						// Fall back to parent
						c = super.loadClass(name, resolve);
					}
				}
			}
			if (resolve) {
				resolveClass(c);
			}
			return c;
		}
	}
}