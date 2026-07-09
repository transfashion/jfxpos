package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.SalespersonController;
import jfxpos.models.Salesperson;

public class SalespersonDialog extends View {
	private static final String Title = "Search Salesperson";
	private static final String FXML = RESOURCE_DIR + "/salesperson.fxml";

	private final Stage stage;
	private final SalespersonController controller = new SalespersonController();

	public SalespersonDialog(Stage owner) throws Exception {
		super(SalespersonDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(true);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public Salesperson getSelectedSalesperson() {
		return controller.getSelectedSalesperson();
	}
}
