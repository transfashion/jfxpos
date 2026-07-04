package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.PromoItemController;
import jfxpos.models.PromoItem;

public class PromoItemDialog extends View {
	static final String Title = "Select Promo Item";
	static final String FXML = RESOURCE_DIR + "/promoitem.fxml";

	final Stage stage;
	final PromoItemController controller = new PromoItemController();

	public PromoItemDialog(Stage owner) throws Exception {
		super(PromoItemDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public PromoItem getSelectedPromoItem() {
		return controller.getSelectedPromoItem();
	}

	public void selectPromoItemById(int promoId) {
		controller.selectPromoItemById(promoId);
	}
}
