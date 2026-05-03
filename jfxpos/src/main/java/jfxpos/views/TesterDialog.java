package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.TesterController;

public class TesterDialog extends View {
    static final String Title = "Configuration";
    static final String FXML = RESOURCE_DIR + "tester.fxml";

    final Stage stage;
    final TesterController controller = new TesterController();


    public TesterDialog(Stage owner) throws Exception {
        super(ConfigDialog.class);

        Scene scene = loadFxml(FXML, controller);
        stage = createDialogStage(Title, scene, owner);
    }

    @Override
    public void openDialog() {
        stage.showAndWait();
    }

}
