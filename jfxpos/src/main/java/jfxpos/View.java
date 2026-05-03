package jfxpos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import jfxpos.util.PosLogger;

import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public abstract class View {
    protected static final String RESOURCE_DIR = "";

    private final AtomicReference<Stage> stageRef = new AtomicReference<>();
    protected final Logger logger;

    // Constructor
    protected View(Class<?> clazz) {
        this.logger = PosLogger.createLogger(clazz.getName());
    }

    protected Stage createStage(String title, Scene scene) {
        Parent root = scene.getRoot();

        Stage stage = new Stage();
        stage.setTitle(title);
        setStage(stage);

        stage.setScene(scene);
        stage.sizeToScene();

        stage.setMinWidth(root.minWidth(-1));
        stage.setMinHeight(root.minHeight(-1));
        stage.setWidth(root.prefWidth(-1));
        stage.setHeight(root.prefHeight(-1));
        stage.setMaxWidth(root.maxWidth(-1));
        stage.setMaxHeight(root.maxHeight(-1));

        stage.centerOnScreen();

        return stage;
    }

    protected Stage createDialogStage(String title, Scene scene, Stage owner) {
        Stage stage = createStage(title, scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        return stage;
    }

    protected void setStage(Stage newStage) {
        if (!stageRef.compareAndSet(null, newStage)) {
            throw new IllegalStateException("Stage sudah pernah di-set!");
        }
    }

    public Stage getStage() {
        return stageRef.get();
    }

    public void open() {
        logger.warning("open is not implemented yet.");
    };

    public void openDialog() {
        logger.warning("openDialog is not implemented yet.");
    };

    protected Scene loadFxml(String fxml, Object controller) throws Exception {
        Scene scene;

        URL fxmlUrl = View.class.getResource(fxml);
        if (fxmlUrl == null) {
            throw new RuntimeException("FXML not found: " + fxml);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setClassLoader(View.class.getClassLoader());
        if (controller != null) {
            loader.setController(controller);
        }

        Parent root = loader.load();
        scene = new Scene(root, root.prefWidth(-1), root.prefHeight(-1));

        return scene;
    }




}
