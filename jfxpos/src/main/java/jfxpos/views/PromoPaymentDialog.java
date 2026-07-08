package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.PromoPaymentController;
import jfxpos.models.PromoPayment;

public class PromoPaymentDialog extends View {
	static final String Title = "Select Promo Payment";
	static final String FXML = RESOURCE_DIR + "/promopayment.fxml";

	final Stage stage;
	final PromoPaymentController controller = new PromoPaymentController();

	public PromoPaymentDialog(Stage owner) throws Exception {
		super(PromoPaymentDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public PromoPayment getSelectedPromoPayment() {
		return controller.getSelectedPromoPayment();
	}

	public void selectPromoPaymentById(int promoId) {
		controller.selectPromoPaymentById(promoId);
	}
}
