package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.PromoNextTxController;
import jfxpos.models.PromoNextTx;

public class PromoNextTxDialog extends View {
	static final String Title = "Select Promo Next Transaction";
	static final String FXML = RESOURCE_DIR + "/promonexttx.fxml";

	final Stage stage;
	final PromoNextTxController controller = new PromoNextTxController();

	public PromoNextTxDialog(Stage owner) throws Exception {
		super(PromoNextTxDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public PromoNextTx getSelectedPromoNextTx() {
		return controller.getSelectedPromoNextTx();
	}

	public void selectPromoNextTxById(int promoId) {
		controller.selectPromoNextTxById(promoId);
	}
}
