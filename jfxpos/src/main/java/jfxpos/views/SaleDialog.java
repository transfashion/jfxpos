package jfxpos.views;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxpos.App;
import jfxpos.View;
import jfxpos.controller.SaleController;

public class SaleDialog extends View {
	static final String Title = "Point of Sale";
	static final String FXML = RESOURCE_DIR + "/sale.fxml";

	final Stage stage;
	final SaleController controller = new SaleController();
	private boolean LINE_INPUT_MODE = true;

	public boolean isLINE_INPUT_MODE() {
		return LINE_INPUT_MODE;
	}

	public void setLINE_INPUT_MODE(boolean LINE_INPUT_MODE) {
		this.LINE_INPUT_MODE = LINE_INPUT_MODE;
	}

	public SaleDialog(Stage owner, int consoleNumber) throws Exception {
		super(SaleDialog.class);

		Scene scene = loadFxml(FXML, controller);
		controller.setConsoleNumber(consoleNumber);

		// Right-click on row shows ContextMenu from itemcontextmenu.fxml, double-click
		// edits qty
		javafx.scene.control.ContextMenu contextMenu = null;
		try {
			javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(View.class.getResource("/itemcontextmenu.fxml"));
			contextMenu = loader.load();

			javafx.scene.control.MenuItem removeMenuItem = (javafx.scene.control.MenuItem) loader.getNamespace()
					.get("removeMenuItem");
			javafx.scene.control.MenuItem editQtyMenuItem = (javafx.scene.control.MenuItem) loader.getNamespace()
					.get("editQtyMenuItem");
			javafx.scene.control.MenuItem infoMenuItem = (javafx.scene.control.MenuItem) loader.getNamespace()
					.get("infoMenuItem");

			if (removeMenuItem != null) {
				removeMenuItem.setOnAction(event -> controller.fireF8Button());
			}
			if (editQtyMenuItem != null) {
				editQtyMenuItem.setOnAction(event -> controller.fireF3Button());
			}
			if (infoMenuItem != null) {
				infoMenuItem.setOnAction(event -> {
					var selected = controller.getItemTable().getSelectionModel().getSelectedItem();
					if (selected != null) {
						logger.info("Selected item details: ID=" + selected.getItemId() + ", Descr="
								+ selected.getItemDescr() + ", Art=" + selected.getItemArt() + ", Col="
								+ selected.getItemCol() + ", Size=" + selected.getItemSize() + ", Price="
								+ selected.getItemPrice() + ", Qty=" + selected.getQty());
					} else {
						logger.info("No item selected for Item Information.");
					}
				});
			}
		} catch (Exception e) {
			logger.severe("Failed to load itemcontextmenu.fxml: " + e.getMessage());
		}

		if (controller.getItemTable() != null && contextMenu != null) {
			final javafx.scene.control.ContextMenu finalContextMenu = contextMenu;
			controller.getItemTable().setRowFactory(tv -> {
				javafx.scene.control.TableRow<jfxpos.models.TrxItem> row = new javafx.scene.control.TableRow<>();

				// Bind context menu to row
				row.contextMenuProperty().bind(
						javafx.beans.binding.Bindings.when(row.emptyProperty())
								.then((javafx.scene.control.ContextMenu) null)
								.otherwise(finalContextMenu));

				// Double-click handler
				row.setOnMouseClicked(event -> {
					if (!row.isEmpty()) {
						if (event.getClickCount() == 2) {
							controller.editCurrentRowQty();
						}
					}
				});
				return row;
			});
		}

		stage = createDialogStage(Title + " - Console #" + consoleNumber, scene, owner);
		stage.setResizable(true);

		if (App.isProd) {
			stage.setMaximized(true);
		} else if (App.isDev) {
			stage.setX(0);
			stage.setY(0);
		}

		// Push initial date/time to controller
		java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter
				.ofPattern("EEEE, d MMMM yyyy", java.util.Locale.of("id", "ID"));
		java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
		java.time.LocalDateTime now = java.time.LocalDateTime.now();
		controller.updateDateTime(now.format(dateFormatter), now.format(timeFormatter));

		// Request focus on lineInput and notify controller when shown
		stage.setOnShown(e -> {
			controller.requestFocus();
			controller.onFocused();
		});

		// Notify controller when stage gains focus
		stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (isNowFocused) {
				controller.onFocused();
			}
		});

		// Redirect key typed to lineInput if LINE_INPUT_MODE is active and focus is
		// elsewhere
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
			if (LINE_INPUT_MODE && !controller.isLineInputFocused()) {
				String character = event.getCharacter();
				if (character != null && character.length() == 1) {
					char c = character.charAt(0);
					// Filter printable characters (exclude control characters)
					if (c >= 32 && c != 127) {
						controller.requestFocus();
						controller.appendLineInput(character);
						event.consume();
					}
				}
			}
		});

		// Execute escButton when Escape key is pressed
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == javafx.scene.input.KeyCode.V) {
				if (!controller.isLineInputFocused()) {
					javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
					if (clipboard.hasString()) {
						controller.requestFocus();
						controller.appendLineInput(clipboard.getString());
					}
					event.consume();
				}
			} else if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
				controller.fireEscButton();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F1) {
				controller.fireF1Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F2) {
				controller.fireF2Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F3) {
				controller.fireF3Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F4) {
				controller.fireF4Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F5) {
				controller.fireF5Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F6) {
				controller.fireF6Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F7) {
				controller.fireF7Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F8) {
				controller.fireF8Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F9) {
				controller.fireF9Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F10) {
				controller.fireF10Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F11) {
				controller.fireF11Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.F12) {
				controller.fireF12Button();
				event.consume();
			} else if (event.getCode() == javafx.scene.input.KeyCode.UP
					|| event.getCode() == javafx.scene.input.KeyCode.DOWN) {
				if (!controller.isItemTableFocused()) {
					controller.focusItemTable();
					event.consume();
				}
			} else if (event.getCode() == javafx.scene.input.KeyCode.LEFT) {
				if (!controller.isLineInputFocused()) {
					controller.handleLeftArrow();
					event.consume();
				}
			} else if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) {
				if (!controller.isLineInputFocused()) {
					controller.handleRightArrow();
					event.consume();
				}
			} else if (event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) {
				if (!controller.isLineInputFocused()) {
					controller.handleBackspace();
					event.consume();
				}
			} else if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
				String text = controller.getLineInputText();
				if (text != null && !text.isEmpty()) {
					searchItem(controller.getCurrentSearchMode(), text);
					event.consume();
				}
			}
		});

		// Intercept stage close request (e.g. clicking 'X' close button) to ask for
		// confirmation
		stage.setOnCloseRequest(event -> {
			if (controller.isHasPendingTransaction()) {
				if (!controller.confirmClose()) {
					event.consume();
				}
			}
		});
	}

	private void searchItem(jfxpos.models.InputSearchMode searchMode, String searchtext) {
		logger.info("Executing searchItem with mode: " + searchMode + ", text: " + searchtext);
		controller.handleItemSearch(searchMode, searchtext);
	}

	@Override
	public void openDialog() {
		stage.showAndWait();
	}
}
