package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.binding.Bindings;
import java.math.BigDecimal;
import jfxpos.models.Trx;
import jfxpos.Controller;
import jfxpos.util.MessageBox;
import jfxpos.views.CustDisplayWindow;

public class SaleController extends Controller {

	private static SaleController activeController;

	private Stage currentWindow;
	private final ObjectProperty<Trx> currentTrx = new SimpleObjectProperty<>();
	private final jfxpos.repository.ChannelRepository channelRepo = new jfxpos.repository.ChannelRepository();

	private final javafx.beans.value.ChangeListener<BigDecimal> grandTotalListener = (obs, oldVal, newVal) -> {
		if (activeController == this) {
			CustDisplayController custDisplay = getCustDisplayController();
			if (custDisplay != null) {
				String formatted = newVal == null ? "0" : String.format("%,.0f", newVal);
				custDisplay.setGrandTotal(formatted);
			}
		}
	};

	@FXML
	private Label storeNameLabel;

	@FXML
	private Label storeInfoLabel;

	@FXML
	private Label channelNameLabel;

	@FXML
	private Label dateLabel;

	@FXML
	private Label timeLabel;

	@FXML
	private ImageView searchModeImage;

	@FXML
	private TextField lineInput;

	@FXML
	private Label itemDescriptionLabel;

	@FXML
	private Label itemPriceLabel;

	@FXML
	private TableView<?> itemTable;

	@FXML
	private Label grandTotalValueLabel;

	@FXML
	private Label grandTotalQtyLabel;

	@FXML
	private Label customerIdLabel;

	@FXML
	private Label customerTypeLabel;

	@FXML
	private Label customerNameLabel;

	@FXML
	private Label customerDiscountLabel;

	@FXML
	private Button btnSearchCustomer;

	@FXML
	private Button btnVipCustomer;

	@FXML
	private Button btnRegisterCustomer;

	@FXML
	private Button btnClearCustomer;

	@FXML
	private Button f1Button;

	@FXML
	private Button f2Button;

	@FXML
	private Button f3Button;

	@FXML
	private Button f4Button;

	@FXML
	private Button f5Button;

	@FXML
	private Button f6Button;

	@FXML
	private Button f7Button;

	@FXML
	private Button f8Button;

	@FXML
	private Button f9Button;

	@FXML
	private Button f10Button;

	@FXML
	private Button f11Button;

	@FXML
	private Button f12Button;

	@FXML
	private Button escButton;

	private int consoleNumber;
	private jfxpos.models.InputSearchMode currentSearchMode = jfxpos.models.InputSearchMode.BARCODE;

	public SaleController() {
		super(SaleController.class);
	}

	public void setConsoleNumber(int consoleNumber) {
		this.consoleNumber = consoleNumber;
		logger.info("SaleController configured for Console #" + consoleNumber);
	}

	public int getConsoleNumber() {
		return consoleNumber;
	}

	public jfxpos.models.InputSearchMode getCurrentSearchMode() {
		return currentSearchMode;
	}

	@FXML
	public void initialize() {
		// Bind components to currentTrx properties dynamically
		currentTrx.addListener((obs, oldTrx, newTrx) -> {
			if (oldTrx != null) {
				oldTrx.grandTotalProperty().removeListener(grandTotalListener);
				if (grandTotalValueLabel != null) {
					grandTotalValueLabel.textProperty().unbind();
				}
				if (grandTotalQtyLabel != null) {
					grandTotalQtyLabel.textProperty().unbind();
				}
				if (channelNameLabel != null) {
					channelNameLabel.textProperty().unbind();
				}
				if (customerIdLabel != null) {
					customerIdLabel.textProperty().unbind();
				}
				if (customerTypeLabel != null) {
					customerTypeLabel.textProperty().unbind();
				}
				if (customerNameLabel != null) {
					customerNameLabel.textProperty().unbind();
				}
				if (customerDiscountLabel != null) {
					customerDiscountLabel.textProperty().unbind();
				}
			}
			if (newTrx != null) {
				newTrx.grandTotalProperty().addListener(grandTotalListener);
				if (activeController == this) {
					grandTotalListener.changed(newTrx.grandTotalProperty(), null, newTrx.getGrandTotal());
				}
				if (grandTotalValueLabel != null) {
					grandTotalValueLabel.textProperty().bind(
							Bindings.format("%,.0f", newTrx.grandTotalProperty()));
				}
				if (grandTotalQtyLabel != null) {
					grandTotalQtyLabel.textProperty().bind(
							Bindings.format("%,d", newTrx.qtyProperty()));
				}
				if (channelNameLabel != null) {
					channelNameLabel.textProperty().bind(newTrx.channelNameProperty());
				}
				if (customerIdLabel != null) {
					customerIdLabel.textProperty().bind(
							Bindings.createStringBinding(
									() -> {
										int cid = newTrx.customerIdProperty().get();
										return cid == 0 ? "" : String.valueOf(cid);
									},
									newTrx.customerIdProperty()));
				}
				if (customerTypeLabel != null) {
					customerTypeLabel.textProperty().bind(
							Bindings.createStringBinding(
									() -> {
										int ctid = newTrx.customerTypeIdProperty().get();
										return ctid == 0 ? "" : String.valueOf(ctid);
									},
									newTrx.customerTypeIdProperty()));
				}
				if (customerNameLabel != null) {
					customerNameLabel.textProperty().bind(
							Bindings.createStringBinding(
									() -> {
										String cname = newTrx.customerNameProperty().get();
										return cname == null ? "" : cname;
									},
									newTrx.customerNameProperty()));
				}
				if (customerDiscountLabel != null) {
					customerDiscountLabel.textProperty().bind(
							Bindings.format("%,.0f", newTrx.customerDiscountProperty()));
				}
			} else {
				if (activeController == this) {
					CustDisplayController custDisplay = getCustDisplayController();
					if (custDisplay != null) {
						custDisplay.setGrandTotal("0");
					}
				}
				if (grandTotalValueLabel != null) {
					grandTotalValueLabel.setText("0");
				}
				if (grandTotalQtyLabel != null) {
					grandTotalQtyLabel.setText("0");
				}
				if (channelNameLabel != null) {
					channelNameLabel.setText("");
				}
				if (customerIdLabel != null) {
					customerIdLabel.setText("");
				}
				if (customerTypeLabel != null) {
					customerTypeLabel.setText("");
				}
				if (customerNameLabel != null) {
					customerNameLabel.setText("");
				}
				if (customerDiscountLabel != null) {
					customerDiscountLabel.setText("0");
				}
			}
		});

		// Set initial prompt text
		if (lineInput != null) {
			lineInput.setPromptText(currentSearchMode.getPrompt());
			lineInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal) {
					javafx.application.Platform.runLater(() -> {
						lineInput.deselect();
						lineInput.end();
					});
				}
			});
		}

		updateSearchModeImage();
		createNewTransaction();

		// Rotate search mode on F1 button click
		if (f1Button != null) {
			f1Button.setOnAction(e -> rotateSearchMode());
		}

		// Open channel selection on F2 button click
		if (f2Button != null) {
			f2Button.setOnAction(e -> openChannelDialog());
		}

		// Show message box on F7 button click
		if (f7Button != null) {
			f7Button.setOnAction(e -> createNewTransaction());
		}

		// Update customer display total on F10 button click
		if (f10Button != null) {
			f10Button.setOnAction(e -> openCheckoutDialog());
		}

		// Close dialog on ESC button click
		if (escButton != null) {
			escButton.setOnAction(e -> closeSaleDialog());
		}
	}

	private void updateSearchModeImage() {
		if (searchModeImage != null && currentSearchMode.getImage() != null) {
			try {
				String imagePath = "/" + currentSearchMode.getImage();
				java.io.InputStream is = getClass().getResourceAsStream(imagePath);
				if (is != null) {
					Image img = new Image(is);
					searchModeImage.setImage(img);
				} else {
					logger.warning("Search mode image not found: " + imagePath);
				}
			} catch (Exception e) {
				logger.severe("Failed to load search mode image: " + e.getMessage());
			}
		}
	}

	private void rotateSearchMode() {
		jfxpos.models.InputSearchMode[] modes = jfxpos.models.InputSearchMode.values();
		int nextOrdinal = (currentSearchMode.ordinal() + 1) % modes.length;
		currentSearchMode = modes[nextOrdinal];
		if (lineInput != null) {
			lineInput.setPromptText(currentSearchMode.getPrompt());
		}
		updateSearchModeImage();
		logger.info("Search mode changed to: " + currentSearchMode);
	}

	public boolean confirmClose() {
		Stage stage = null;
		if (escButton != null && escButton.getScene() != null) {
			stage = (Stage) escButton.getScene().getWindow();
		}
		return MessageBox.confirm(stage, "Apakah akan menutup console ini?");
	}

	public void requestFocus() {
		if (lineInput != null) {
			javafx.application.Platform.runLater(() -> lineInput.requestFocus());
		}
	}

	public boolean isLineInputFocused() {
		return lineInput != null && lineInput.isFocused();
	}

	public boolean isItemTableFocused() {
		return itemTable != null && itemTable.isFocused();
	}

	public void focusItemTable() {
		if (itemTable != null) {
			itemTable.requestFocus();
			if (itemTable.getSelectionModel().getSelectedIndex() < 0 && !itemTable.getItems().isEmpty()) {
				itemTable.getSelectionModel().select(0);
			}
		}
	}

	public void appendLineInput(String text) {
		if (lineInput != null) {
			lineInput.appendText(text);
			lineInput.positionCaret(lineInput.getText().length());
		}
	}

	public void handleLeftArrow() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				if (pos > 0) {
					lineInput.positionCaret(pos - 1);
				}
			});
		}
	}

	public void handleRightArrow() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				if (pos < lineInput.getText().length()) {
					lineInput.positionCaret(pos + 1);
				}
			});
		}
	}

	public void handleBackspace() {
		if (lineInput != null) {
			lineInput.requestFocus();
			javafx.application.Platform.runLater(() -> {
				int pos = lineInput.getCaretPosition();
				String text = lineInput.getText();
				if (pos > 0 && text != null && !text.isEmpty()) {
					lineInput.deleteText(pos - 1, pos);
				}
			});
		}
	}

	public void fireF1Button() {
		if (f1Button != null) {
			f1Button.fire();
		}
	}

	public void fireF2Button() {
		if (f2Button != null) {
			f2Button.fire();
		}
	}

	public void fireF3Button() {
		if (f3Button != null) {
			f3Button.fire();
		}
	}

	public void fireF4Button() {
		if (f4Button != null) {
			f4Button.fire();
		}
	}

	public void fireF5Button() {
		if (f5Button != null) {
			f5Button.fire();
		}
	}

	public void fireF6Button() {
		if (f6Button != null) {
			f6Button.fire();
		}
	}

	public void fireF7Button() {
		if (f7Button != null) {
			f7Button.fire();
		}
	}

	public void fireF8Button() {
		if (f8Button != null) {
			f8Button.fire();
		}
	}

	public void fireF9Button() {
		if (f9Button != null) {
			f9Button.fire();
		}
	}

	public void fireF10Button() {
		if (f10Button != null) {
			f10Button.fire();
		}
	}

	public void fireF11Button() {
		if (f11Button != null) {
			f11Button.fire();
		}
	}

	public void fireF12Button() {
		if (f12Button != null) {
			f12Button.fire();
		}
	}

	public void fireEscButton() {
		if (escButton != null) {
			escButton.fire();
		}
	}

	private Stage getCurrentWindow() {
		if (currentWindow == null && f1Button != null && f1Button.getScene() != null) {
			currentWindow = (Stage) f1Button.getScene().getWindow();
		}
		return currentWindow;
	}

	public boolean isHasPendingTransaction() {
		Trx current = currentTrx.get();
		if (current != null) {
			BigDecimal grandTotal = current.getGrandTotal();
			return grandTotal != null && grandTotal.compareTo(BigDecimal.ZERO) != 0;
		}
		return false;
	}

	public void createNewTransaction() {
		if (isHasPendingTransaction()) {
			boolean confirm = MessageBox.confirm(getCurrentWindow(), 
					"Sedang ada transaksi di console saat ini, apakah yakin akan create transaksi baru?");
			if (!confirm) {
				return;
			}
		}

		Trx newTrx = new Trx();
		newTrx.setSubtotal(BigDecimal.ZERO);
		newTrx.setQty(0);

		try {
			java.util.List<jfxpos.models.Channel> channels = channelRepo.findAll();
			if (channels != null && !channels.isEmpty()) {
				jfxpos.models.Channel smallestIdChannel = channels.stream()
						.min(java.util.Comparator.comparingInt(c -> c.getId()))
						.orElse(null);
				if (smallestIdChannel != null) {
					newTrx.setChannelId(smallestIdChannel.getId());
					newTrx.setChannelName(smallestIdChannel.getChannelName());
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to load default channel for new transaction: " + e.getMessage());
		}

		currentTrx.set(newTrx);
		if (lineInput != null) {
			lineInput.clear();
		}
		if (itemDescriptionLabel != null) {
			itemDescriptionLabel.setText("");
		}
		if (itemPriceLabel != null) {
			itemPriceLabel.setText("");
		}
		logger.info("New transaction started");
	}

	private void openChannelDialog() {
		try {
			jfxpos.views.ChannelDialog dialog = new jfxpos.views.ChannelDialog(getCurrentWindow());
			Trx trx = currentTrx.get();
			if (trx != null && trx.getChannelId() != null) {
				dialog.selectChannelById(trx.getChannelId());
			}
			dialog.openDialog();
			jfxpos.models.Channel selected = dialog.getSelectedChannel();
			if (selected != null) {
				logger.info("Selected Channel: " + selected.getChannelName());
				if (trx != null) {
					trx.setChannelId(selected.getId());
					trx.setChannelName(selected.getChannelName());
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open ChannelDialog: " + e.getMessage());
		}
	}

	private void openCheckoutDialog() {
		Trx trx = currentTrx.get();
		trx.setGrandTotal(BigDecimal.valueOf(10000));

	}

	private void closeSaleDialog() {
		if (escButton.getScene() != null && escButton.getScene().getWindow() instanceof Stage stage) {
			if (isHasPendingTransaction()) {
				if (confirmClose()) {
					stage.close();
				}
			} else {
				stage.close();
			}
		}
	}

	public void updateDateTime(String dateText, String timeText) {
		if (dateLabel != null) {
			dateLabel.setText(dateText);
		}
		if (timeLabel != null) {
			timeLabel.setText(timeText);
		}
	}

	public void onFocused() {
		activeController = this;
		updateCustDisplay();
	}

	public void updateCustDisplay() {
		CustDisplayController custDisplay = getCustDisplayController();
		if (custDisplay != null) {
			Trx trx = currentTrx.get();
			BigDecimal grandTotal = (trx != null) ? trx.getGrandTotal() : BigDecimal.ZERO;
			String formatted = grandTotal == null ? "0" : String.format("%,.0f", grandTotal);
			custDisplay.setGrandTotal(formatted);
		}
	}

	private CustDisplayController getCustDisplayController() {
		CustDisplayWindow window = CustDisplayWindow.getInstance();
		return window != null ? window.getController() : null;
	}
}
