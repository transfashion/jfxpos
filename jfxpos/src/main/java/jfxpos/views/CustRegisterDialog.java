package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.CustRegisterController;

public class CustRegisterDialog extends View {
	private static final String Title = "Register Customer";
	private static final String FXML = RESOURCE_DIR + "/custregister.fxml";

	private final Stage stage;
	private final CustRegisterController controller = new CustRegisterController();

	public CustRegisterDialog(Stage owner) throws Exception {
		super(CustRegisterDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public boolean isSaved() {
		return controller.isSaved();
	}

	public String getCustomerId() {
		return controller.getCustomerId();
	}

	public String getCustomerName() {
		return controller.getCustomerName();
	}

	public void setCustomerId(String id) {
		controller.setCustomerId(id);
	}

	public void setCustomerName(String name) {
		controller.setCustomerName(name);
	}

	public String getCustomerGender() {
		return controller.getCustomerGender();
	}

	public java.time.LocalDate getCustomerBirthdate() {
		return controller.getCustomerBirthdate();
	}

	public void setCustomerGender(String gender) {
		controller.setCustomerGender(gender);
	}

	public void setCustomerBirthdate(java.time.LocalDate birthdate) {
		controller.setCustomerBirthdate(birthdate);
	}
}
