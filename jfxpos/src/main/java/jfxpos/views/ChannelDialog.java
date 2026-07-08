package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.View;
import jfxpos.controller.ChannelController;
import jfxpos.models.Channel;

public class ChannelDialog extends View {
	static final String Title = "Select Channel";
	static final String FXML = RESOURCE_DIR + "/channel.fxml";

	final Stage stage;
	final ChannelController controller = new ChannelController();

	public ChannelDialog(Stage owner) throws Exception {
		super(ChannelDialog.class);

		Scene scene = loadFxml(FXML, controller);
		stage = createDialogStage(Title, scene, owner);
		stage.setResizable(false);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}

	public Channel getSelectedChannel() {
		return controller.getSelectedChannel();
	}

	public void selectChannelById(int channelId) {
		controller.selectChannelById(channelId);
	}
}
