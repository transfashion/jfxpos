package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.CustSearchController;
import jfxpos.models.Customer;

public class CustSearchDialog extends View {
	private static final String Title = "Search Customer";
	private static final String FXML = RESOURCE_DIR + "/custsearch.fxml";

	private final Stage stage;
	private final CustSearchController controller = new CustSearchController();

	public CustSearchDialog(Stage owner) throws Exception {
		super(CustSearchDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(true);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public Customer getSelectedCustomer() {
		return controller.getSelectedCustomer();
	}
}
