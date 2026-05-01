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


public class Launcher extends Application {

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

        // start async (pakai virtual thread Java 21)
        Thread.startVirtualThread(loadTask);
    }

    // =========================
    // SPLASH UI
    // =========================
    private SplashView createSplash(Stage stage) {
        Label title = new Label("JFX Point of Sales");
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
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bag.png"))));

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

                updateMessage("Loading updater...");
                Updater.update(this::updateMessage);

//                runUpdaterSafe(this);

                updateMessage("Preparing classloader...");
                Path jarPath = getJarPath();
                URL[] urls = { jarPath.toUri().toURL() };

                appClassLoader = new URLClassLoader(urls, Launcher.class.getClassLoader());
                registerShutdownHook();

                updateMessage("Loading application class...");
                Class<?> appClass = appClassLoader.loadClass("jfxpos.App");

                updateMessage("Reading configuration...");
                Method configMethod = appClass.getMethod("readConfiguration");
                configMethod.invoke(null);

                updateMessage("Starting application...");
                return appClass;
            }
        };
    }

//    private void runUpdaterSafe(Task<?> task) {
//        try {
//            // ⚠️ pastikan Updater tidak update UI langsung!
//            Updater.update(task::updateMessage);
////            Updater.update(msg -> {
////                // kalau kamu ubah Updater jadi callback-based
////                Platform.runLater(() -> {
////                    // optional logging / debug
////                    System.out.println(msg);
////                });
////            });
//        } catch (Exception e) {
//            throw new RuntimeException("Updater failed", e);
//        }
//    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (appClassLoader != null) {
                    System.out.println("Closing classloader...");
                    appClassLoader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
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

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bag.png"))));

        primaryStage.toFront();
        primaryStage.requestFocus();

        closeSplash(splashRoot, splashStage, primaryStage);
    }

    // =========================
    // ERROR HANDLING
    // =========================
    private void showErrorAndExit(Throwable e, Stage splashStage) {
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
        Path jfxposCfg = basePath.resolve("app/jfxpos.cfg");
        Path jfxposJar = basePath.resolve("app/jfxpos.jar");
        Path ideJar = Paths.get("build/libs/jfxpos.jar").toAbsolutePath();

        if (Files.exists(jfxposCfg)) {
            // baca dulu dari konfigurasi file
            String fileName = getJfxposFile(jfxposCfg);
            jarPath = basePath.resolve("app/" + fileName);
            if (!Files.exists(jarPath)) {
                throw new RuntimeException("Tidak menemukan '" + jarPath + "'!");
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
        String jfxpos;
        jfxpos = props.getProperty("jfxpos", "jfxpos.jar");
        return jfxpos;
    }



    private void closeSplash(VBox root, Stage splash, Stage main) {
        splash.close();
        main.show();
    }

    private Alert createAlert(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(e.getMessage());
        return alert;
    }
}