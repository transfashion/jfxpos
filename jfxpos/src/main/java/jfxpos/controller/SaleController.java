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
	private Label deviceNumLabel;

	@FXML
	private Label siteNameLabel;

	@FXML
	private Label nameLabel;

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
	private Button searchCustomerButton;

	@FXML
	private Button vipCustomerButton;

	@FXML
	private Button registerCustomerButton;

	@FXML
	private Button clearCustomerButton;

	@FXML
	private Label promoItemCountLabel;

	@FXML
	private Label promoItemIdLabel;

	@FXML
	private Label promoItemNameLabel;

	@FXML
	private Label promoItemDescrLabel;

	@FXML
	private Label promoPaymCountLabel;

	@FXML
	private Label promoPaymIdLabel;

	@FXML
	private Label promoPaymNameLabel;

	@FXML
	private Label promoPaymDescrLabel;

	@FXML
	private Label promoNextTxCountLabel;

	@FXML
	private Label promoNextTxIdLabel;

	@FXML
	private Label promoNextTxNameLabel;

	@FXML
	private Label promoNextTxDescrLabel;

	@FXML
	private Button promoItemButton;

	@FXML
	private Button promoPaymentButton;

	@FXML
	private Button promoNextTxButton;

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
		if (siteNameLabel != null && jfxpos.App.config != null) {
			siteNameLabel.setText(jfxpos.App.config.siteName());
		}
		if (nameLabel != null && jfxpos.App.config != null) {
			nameLabel.setText(jfxpos.App.config.name());
		}
		if (deviceNumLabel != null && jfxpos.App.config != null && jfxpos.App.config.deviceNum() != null) {
			deviceNumLabel.setText(jfxpos.App.config.deviceNum());
		}

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
				if (promoItemCountLabel != null) {
					promoItemCountLabel.textProperty().unbind();
				}
				if (promoItemIdLabel != null) {
					promoItemIdLabel.textProperty().unbind();
				}
				if (promoItemNameLabel != null) {
					promoItemNameLabel.textProperty().unbind();
				}
				if (promoItemDescrLabel != null) {
					promoItemDescrLabel.textProperty().unbind();
				}
				if (promoPaymCountLabel != null) {
					promoPaymCountLabel.textProperty().unbind();
				}
				if (promoPaymIdLabel != null) {
					promoPaymIdLabel.textProperty().unbind();
				}
				if (promoPaymNameLabel != null) {
					promoPaymNameLabel.textProperty().unbind();
				}
				if (promoPaymDescrLabel != null) {
					promoPaymDescrLabel.textProperty().unbind();
				}
				if (promoNextTxCountLabel != null) {
					promoNextTxCountLabel.textProperty().unbind();
				}
				if (promoNextTxIdLabel != null) {
					promoNextTxIdLabel.textProperty().unbind();
				}
				if (promoNextTxNameLabel != null) {
					promoNextTxNameLabel.textProperty().unbind();
				}
				if (promoNextTxDescrLabel != null) {
					promoNextTxDescrLabel.textProperty().unbind();
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
										long cid = newTrx.customerIdProperty().get();
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
				if (promoItemCountLabel != null) {
					promoItemCountLabel.textProperty().bind(Bindings.format("%,d", newTrx.promoItemCountProperty()));
				}
				if (promoItemIdLabel != null) {
					promoItemIdLabel.textProperty().bind(Bindings.createStringBinding(
							() -> {
								long id = newTrx.promoItemIdProperty().get();
								return id == 0 ? "" : String.valueOf(id);
							},
							newTrx.promoItemIdProperty()));
				}
				if (promoItemNameLabel != null) {
					promoItemNameLabel.textProperty().bind(newTrx.promoItemNameProperty());
				}
				if (promoItemDescrLabel != null) {
					promoItemDescrLabel.textProperty().bind(newTrx.promoItemDescrProperty());
				}

				if (promoPaymCountLabel != null) {
					promoPaymCountLabel.textProperty().bind(Bindings.format("%,d", newTrx.promoPaymCountProperty()));
				}
				if (promoPaymIdLabel != null) {
					promoPaymIdLabel.textProperty().bind(Bindings.createStringBinding(
							() -> {
								long id = newTrx.promoPaymIdProperty().get();
								return id == 0 ? "" : String.valueOf(id);
							},
							newTrx.promoPaymIdProperty()));
				}
				if (promoPaymNameLabel != null) {
					promoPaymNameLabel.textProperty().bind(newTrx.promoPaymNameProperty());
				}
				if (promoPaymDescrLabel != null) {
					promoPaymDescrLabel.textProperty().bind(newTrx.promoPaymDescrProperty());
				}

				if (promoNextTxCountLabel != null) {
					promoNextTxCountLabel.textProperty().bind(Bindings.format("%,d", newTrx.promoNextTxCountProperty()));
				}
				if (promoNextTxIdLabel != null) {
					promoNextTxIdLabel.textProperty().bind(Bindings.createStringBinding(
							() -> {
								long id = newTrx.promoNextTxIdProperty().get();
								return id == 0 ? "" : String.valueOf(id);
							},
							newTrx.promoNextTxIdProperty()));
				}
				if (promoNextTxNameLabel != null) {
					promoNextTxNameLabel.textProperty().bind(newTrx.promoNextTxNameProperty());
				}
				if (promoNextTxDescrLabel != null) {
					promoNextTxDescrLabel.textProperty().bind(newTrx.promoNextTxDescrProperty());
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
				if (promoItemCountLabel != null) {
					promoItemCountLabel.setText("0");
				}
				if (promoItemIdLabel != null) {
					promoItemIdLabel.setText("");
				}
				if (promoItemNameLabel != null) {
					promoItemNameLabel.setText("");
				}
				if (promoItemDescrLabel != null) {
					promoItemDescrLabel.setText("");
				}
				if (promoPaymCountLabel != null) {
					promoPaymCountLabel.setText("0");
				}
				if (promoPaymIdLabel != null) {
					promoPaymIdLabel.setText("");
				}
				if (promoPaymNameLabel != null) {
					promoPaymNameLabel.setText("");
				}
				if (promoPaymDescrLabel != null) {
					promoPaymDescrLabel.setText("");
				}
				if (promoNextTxCountLabel != null) {
					promoNextTxCountLabel.setText("0");
				}
				if (promoNextTxIdLabel != null) {
					promoNextTxIdLabel.setText("");
				}
				if (promoNextTxNameLabel != null) {
					promoNextTxNameLabel.setText("");
				}
				if (promoNextTxDescrLabel != null) {
					promoNextTxDescrLabel.setText("");
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

		// Edit Current Row Qty
		if (f3Button != null) {
			f3Button.setOnAction(e -> editCurrentRowQty());
		}

		// Select Promo for Next Transaction
		if (f4Button != null) {
			f4Button.setOnAction(e -> openPromoNextTxDialog());
		}

		// Select Payment Promo
		if (f5Button != null) {
			f5Button.setOnAction(e -> openPromoPaymentDialog());
		}

		// Select Item Promo
		if (f6Button != null) {
			f6Button.setOnAction(e -> openPromoItemDialog());
		}

		// Show message box on F7 button click
		if (f7Button != null) {
			f7Button.setOnAction(e -> createNewTransaction());
		}

		// Remove current Row
		if (f8Button != null) {
			f8Button.setOnAction(e -> removeCurrentRow());
		}

		// Recall
		if (f9Button != null) {
			f9Button.setOnAction(e -> openRecallDialog());
		}

		// Checkout
		if (f10Button != null) {
			f10Button.setOnAction(e -> openCheckoutDialog());
		}

		if (f11Button != null) {
			f11Button.setOnAction(e -> openCustSearchDialog());
		}

		if (f12Button != null) {
			f12Button.setOnAction(e -> openCustRegisterDialog());
		}

		// Register customer on button click
		if (registerCustomerButton != null) {
			registerCustomerButton.setOnAction(e -> openCustRegisterDialog());
		}

		// Search customer on button click
		if (searchCustomerButton != null) {
			searchCustomerButton.setOnAction(e -> openCustSearchDialog());
		}

		// Clear customer on button click
		if (clearCustomerButton != null) {
			clearCustomerButton.setOnAction(e -> clearCustomerData());
		}

		// promo item
		if (promoItemButton != null) {
			promoItemButton.setOnAction(e -> openPromoItemDialog());
		}

		// promo payment
		if (promoPaymentButton != null) {
			promoPaymentButton.setOnAction(e -> openPromoPaymentDialog());
		}

		// promo next tx
		if (promoNextTxButton != null) {
			promoNextTxButton.setOnAction(e -> openPromoNextTxDialog());
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

		try {
			jfxpos.repository.PromoItemRepository promoItemRepo = new jfxpos.repository.PromoItemRepository();
			jfxpos.repository.PromoPaymentRepository promoPaymentRepo = new jfxpos.repository.PromoPaymentRepository();
			jfxpos.repository.PromoNextTxRepository promoNextTxRepo = new jfxpos.repository.PromoNextTxRepository();

			newTrx.setPromoItemCount(promoItemRepo.getActivePromoCount());
			newTrx.setPromoPaymCount(promoPaymentRepo.getActivePromoCount());
			newTrx.setPromoNextTxCount(promoNextTxRepo.getActivePromoCount());
		} catch (Exception e) {
			logger.severe("Failed to load active promo counts for new transaction: " + e.getMessage());
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

	private void openCustRegisterDialog() {
		try {
			jfxpos.views.CustRegisterDialog dialog = new jfxpos.views.CustRegisterDialog(getCurrentWindow());
			Trx trx = currentTrx.get();
			if (trx != null) {
				Long cid = trx.getCustomerId();
				if (cid != null && cid != 0) {
					dialog.setCustomerId(String.valueOf(cid));
				}
				String cname = trx.getCustomerName();
				if (cname != null && !"NONE".equals(cname)) {
					dialog.setCustomerName(cname);
				}
				Integer cgender = trx.getCustomerGender();
				if (cgender != null && cgender != 0) {
					dialog.setCustomerGender(String.valueOf(cgender));
				}
				java.time.LocalDate cbirth = trx.getCustomerBirthdate();
				if (cbirth != null) {
					dialog.setCustomerBirthdate(cbirth);
				}
			}
			dialog.openDialog();
			if (dialog.isSaved()) {
				String newId = dialog.getCustomerId();
				String newName = dialog.getCustomerName();
				String newGender = dialog.getCustomerGender();
				java.time.LocalDate newBirth = dialog.getCustomerBirthdate();
				logger.info("Registered Customer: ID=" + newId + ", Name=" + newName + ", Gender=" + newGender
						+ ", Birthdate=" + newBirth);
				if (trx != null) {
					trx.setCustomerId(Long.parseLong(newId));
					trx.setCustomerName(newName);
					trx.setCustomerGender(newGender != null && !newGender.isEmpty() ? Integer.parseInt(newGender) : 0);
					trx.setCustomerBirthdate(newBirth);
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open CustRegisterDialog: " + e.getMessage());
		}
	}

	private void openCustSearchDialog() {
		try {
			jfxpos.views.CustSearchDialog dialog = new jfxpos.views.CustSearchDialog(getCurrentWindow());
			dialog.openDialog();
			jfxpos.models.Customer selected = dialog.getSelectedCustomer();
			if (selected != null) {
				logger.info(
						"Selected Customer: ID=" + selected.getCustomerId() + ", Name=" + selected.getCustomerName());
				Trx trx = currentTrx.get();
				if (trx != null) {
					trx.setCustomerId(selected.getCustomerId());
					trx.setCustomerName(selected.getCustomerName());
					trx.setCustomerTypeId(selected.getCustomerTypeId());
					trx.setCustomerTypeName(selected.getCustomerTypeName());
					trx.setCustomerGender(selected.getCustomerGender());
					trx.setCustomerBirthdate(selected.getCustomerBirthdate());
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open CustSearchDialog: " + e.getMessage());
		}
	}

	private void clearCustomerData() {
		Trx trx = currentTrx.get();
		if (trx != null) {
			Long cid = trx.getCustomerId();
			String cname = trx.getCustomerName();
			boolean hasCustomer = (cid != null && cid != 0L) || (cname != null && !"NONE".equalsIgnoreCase(cname));

			if (hasCustomer) {
				boolean confirm = MessageBox.confirm(getCurrentWindow(),
						"Apakah Anda yakin ingin menghapus data customer dari transaksi ini?",
						"Konfirmasi Hapus Customer");
				if (!confirm) {
					return;
				}
			}

			trx.setCustomerId(0L);
			trx.setCustomerName("NONE");
			trx.setCustomerGender(0);
			trx.setCustomerBirthdate(null);
			trx.setCustomerTypeId(0);
			trx.setCustomerTypeName("");
			trx.setCustomerDiscount(java.math.BigDecimal.ZERO);
			logger.info("Customer data cleared from transaction");
		}
	}

	private void openCheckoutDialog() {
		// Trx trx = currentTrx.get();
		// trx.setGrandTotal(BigDecimal.valueOf(10000));
		logger.info("open checkout dialog");

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

	private void openPromoItemDialog() {
		try {
			jfxpos.views.PromoItemDialog dialog = new jfxpos.views.PromoItemDialog(getCurrentWindow());
			Trx trx = currentTrx.get();
			if (trx != null && trx.getPromoItemId() != null) {
				dialog.selectPromoItemById(trx.getPromoItemId().intValue());
			}
			dialog.openDialog();
			jfxpos.models.PromoItem selected = dialog.getSelectedPromoItem();
			if (selected != null) {
				logger.info("Selected Promo Item: " + selected.getNote());
				if (trx != null) {
					trx.setPromoItemId((long) selected.getId());
					trx.setPromoItemName(selected.getNote());
					trx.setPromoItemCode("PRM-ITEM-" + selected.getId());
					trx.setPromoItemDescr("Description for promo " + selected.getId());
					trx.setPromoItemCount(1);
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open PromoItemDialog: " + e.getMessage());
		}
	}

	private void openPromoPaymentDialog() {
		try {
			jfxpos.views.PromoPaymentDialog dialog = new jfxpos.views.PromoPaymentDialog(getCurrentWindow());
			Trx trx = currentTrx.get();
			if (trx != null && trx.getPromoPaymId() != null) {
				dialog.selectPromoPaymentById(trx.getPromoPaymId().intValue());
			}
			dialog.openDialog();
			jfxpos.models.PromoPayment selected = dialog.getSelectedPromoPayment();
			if (selected != null) {
				logger.info("Selected Promo Payment: " + selected.getNote());
				if (trx != null) {
					trx.setPromoPaymId((long) selected.getId());
					trx.setPromoPaymName(selected.getNote());
					trx.setPromoPaymCode("PRM-PAYM-" + selected.getId());
					trx.setPromoPaymDescr("Description for payment promo " + selected.getId());
					trx.setPromoPaymCount(1);
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open PromoPaymentDialog: " + e.getMessage());
		}
	}

	private void openPromoNextTxDialog() {
		try {
			jfxpos.views.PromoNextTxDialog dialog = new jfxpos.views.PromoNextTxDialog(getCurrentWindow());
			Trx trx = currentTrx.get();
			if (trx != null && trx.getPromoNextTxId() != null) {
				dialog.selectPromoNextTxById(trx.getPromoNextTxId().intValue());
			}
			dialog.openDialog();
			jfxpos.models.PromoNextTx selected = dialog.getSelectedPromoNextTx();
			if (selected != null) {
				logger.info("Selected Promo Next Tx: " + selected.getNote());
				if (trx != null) {
					trx.setPromoNextTxId((long) selected.getId());
					trx.setPromoNextTxName(selected.getNote());
					trx.setPromoNextTxCode("PRM-NEXTTX-" + selected.getId());
					trx.setPromoNextTxDescr("Description for next purchase promo " + selected.getId());
					trx.setPromoNextTxCount(1);
				}
			}
		} catch (Exception e) {
			logger.severe("Failed to open PromoNextTxDialog: " + e.getMessage());
		}
	}

	private void openRecallDialog() {
		logger.info("open recall dialog");
	}

	private void editCurrentRowQty() {
		logger.info("edit current qty");
	}

	private void removeCurrentRow() {
		logger.info("remove current row");
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
