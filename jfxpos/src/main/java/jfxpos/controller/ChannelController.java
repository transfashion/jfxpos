package jfxpos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfxpos.Controller;
import jfxpos.models.Channel;
import jfxpos.repository.ChannelRepository;

import java.util.List;

public class ChannelController extends Controller {
	@FXML
	private TableView<Channel> channelTable;

	@FXML
	private TableColumn<Channel, Integer> colId;

	@FXML
	private TableColumn<Channel, String> colName;

	@FXML
	private Button btnSelect;

	@FXML
	private Button btnCancel;

	private final ChannelRepository channelRepo = new ChannelRepository();
	private Channel selectedChannel = null;

	public ChannelController() {
		super(ChannelController.class);
	}

	@FXML
	public void initialize() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<>("channelName"));

		loadChannels();

		// Handle select button action
		btnSelect.setOnAction(e -> confirmSelection());

		// Handle cancel button action
		btnCancel.setOnAction(e -> closeDialog());

		// Double-click row to select
		channelTable.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				confirmSelection();
			}
		});

		// Key events within TableView
		channelTable.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				confirmSelection();
				event.consume();
			} else if (event.getCode() == KeyCode.ESCAPE) {
				closeDialog();
				event.consume();
			}
		});

		// Auto-focus table on load
		javafx.application.Platform.runLater(() -> channelTable.requestFocus());
	}

	private void loadChannels() {
		try {
			List<Channel> list = channelRepo.findAll();
			// Filter only active channels
			List<Channel> activeChannels = list.stream().filter(c -> c.isActive()).toList();
			ObservableList<Channel> data = FXCollections.observableArrayList(activeChannels);
			channelTable.setItems(data);
			if (!data.isEmpty()) {
				channelTable.getSelectionModel().select(0);
			}
		} catch (Exception e) {
			logger.severe("Failed to load channels: " + e.getMessage());
		}
	}

	private void confirmSelection() {
		selectedChannel = channelTable.getSelectionModel().getSelectedItem();
		if (selectedChannel != null) {
			closeDialog();
		}
	}

	private void closeDialog() {
		if (channelTable.getScene() != null && channelTable.getScene().getWindow() instanceof Stage stage) {
			stage.close();
		}
	}

	public Channel getSelectedChannel() {
		return selectedChannel;
	}
}
