package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.math.BigDecimal;

import jfxpos.models.Trx;
import jfxpos.models.TrxItem;
import jfxpossyn.model.Item;
import jfxpos.views.SelectColDialog;
import jfxpos.views.SelectSizeDialog;
import jfxpos.Controller;
import jfxpos.util.MessageBox;
import jfxpos.views.CustDisplayWindow;

public class SaleController extends Controller {

	private static SaleController activeController;

	public static SaleController getActiveController() {
		return activeController;
	}

	private Stage currentWindow;
	private final ObjectProperty<Trx> currentTrx = new SimpleObjectProperty<>();
	private final SaleService saleService = new SaleService();

	private final javafx.beans.value.ChangeListener<BigDecimal> grandTotalListener = (obs, oldVal, newVal) -> {
		if (activeController == this) {
			CustDisplayController custDisplay = getCustDisplayController();
			if (custDisplay != null) {
				String formatted = newVal == null ? "0" : String.format("%,.0f", newVal);
				custDisplay.setGrandTotal(formatted);
			}
		}
	};

	private final javafx.beans.value.ChangeListener<String> customerNameListener = (obs, oldVal, newVal) -> {
		if (activeController == this) {
			CustDisplayController custDisplay = getCustDisplayController();
			if (custDisplay != null) {
				String displayName = (newVal == null || "NONE".equalsIgnoreCase(newVal.trim())) ? "" : newVal;
				custDisplay.setCustomerName(displayName);
			}
		}
	};

	@FXML
	Label deviceNumLabel;

	@FXML
	Label siteNameLabel;

	@FXML
	Label nameLabel;

	@FXML
	Label channelNameLabel;

	@FXML
	Label dateLabel;

	@FXML
	Label timeLabel;

	@FXML
	ImageView searchModeImage;

	@FXML
	TextField lineInput;

	@FXML
	Label itemDescriptionLabel;

	@FXML
	Label itemPriceLabel;

	@FXML
	TableView<TrxItem> itemTable;

	@FXML
	TableColumn<TrxItem, String> colBarcode;

	@FXML
	TableColumn<TrxItem, String> colArticle;

	@FXML
	TableColumn<TrxItem, String> colColor;

	@FXML
	TableColumn<TrxItem, String> colSize;

	@FXML
	TableColumn<TrxItem, String> colDescr;

	@FXML
	TableColumn<TrxItem, Integer> colQty;

	@FXML
	TableColumn<TrxItem, BigDecimal> colPrice;

	@FXML
	TableColumn<TrxItem, BigDecimal> colDisc;

	@FXML
	TableColumn<TrxItem, BigDecimal> colTotal;

	@FXML
	Label grandTotalValueLabel;

	@FXML
	Label grandTotalQtyLabel;

	@FXML
	Label customerIdLabel;

	@FXML
	Label customerTypeLabel;

	@FXML
	Label customerNameLabel;

	@FXML
	Label customerDiscountLabel;

	@FXML
	Button searchCustomerButton;

	@FXML
	Button vipCustomerButton;

	@FXML
	Button registerCustomerButton;

	@FXML
	Button clearCustomerButton;

	@FXML
	Label promoItemCountLabel;

	@FXML
	Label promoItemIdLabel;

	@FXML
	Label promoItemNameLabel;

	@FXML
	Label promoItemDescrLabel;

	@FXML
	Label promoPaymCountLabel;

	@FXML
	Label promoPaymIdLabel;

	@FXML
	Label promoPaymNameLabel;

	@FXML
	Label promoPaymDescrLabel;

	@FXML
	Label promoNextTxCountLabel;

	@FXML
	Label promoNextTxIdLabel;

	@FXML
	Label promoNextTxNameLabel;

	@FXML
	Label promoNextTxDescrLabel;

	@FXML
	Button promoItemButton;

	@FXML
	Button promoPaymentButton;

	@FXML
	Button promoNextTxButton;

	@FXML
	Button f1Button;

	@FXML
	Button f2Button;

	@FXML
	Button f3Button;

	@FXML
	Button f4Button;

	@FXML
	Button f5Button;

	@FXML
	Button f6Button;

	@FXML
	Button f7Button;

	@FXML
	Button f8Button;

	@FXML
	Button f9Button;

	@FXML
	Button f10Button;

	@FXML
	Button f11Button;

	@FXML
	Button f12Button;

	@FXML
	Button escButton;

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
		if (colBarcode != null)
			colBarcode.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("barcode"));
		if (colArticle != null)
			colArticle.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemArt"));
		if (colColor != null)
			colColor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemCol"));
		if (colSize != null)
			colSize.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemSize"));
		if (colDescr != null)
			colDescr.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemDescr"));
		if (colQty != null)
			colQty.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
		if (colPrice != null)
			colPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("itemPrice"));
		if (colDisc != null)
			colDisc.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("discValue"));
		if (colTotal != null)
			colTotal.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("total"));

		if (siteNameLabel != null && jfxpos.App.config != null) {
			siteNameLabel.setText(jfxpos.App.config.siteName());
		}
		if (nameLabel != null && jfxpos.App.config != null) {
			nameLabel.setText(jfxpos.App.config.name());
		}
		if (deviceNumLabel != null && jfxpos.App.config != null && jfxpos.App.config.deviceNum() != null) {
			deviceNumLabel.setText(jfxpos.App.config.deviceNum());
		}

		// Bind components to currentTrx properties dynamically using SaleViewBinder
		currentTrx.addListener((obs, oldTrx, newTrx) -> {
			if (oldTrx != null) {
				SaleViewBinder.unbind(this, oldTrx, grandTotalListener);
				oldTrx.customerNameProperty().removeListener(customerNameListener);
			}
			if (newTrx != null) {
				SaleViewBinder.bind(this, newTrx, grandTotalListener);
				newTrx.customerNameProperty().addListener(customerNameListener);
				if (activeController == this) {
					grandTotalListener.changed(newTrx.grandTotalProperty(), null, newTrx.getGrandTotal());
					customerNameListener.changed(newTrx.customerNameProperty(), null, newTrx.getCustomerName());
				}
			} else {
				if (activeController == this) {
					CustDisplayController custDisplay = getCustDisplayController();
					if (custDisplay != null) {
						custDisplay.setGrandTotal("0");
						custDisplay.setCustomerName("");
					}
				}
				SaleViewBinder.clear(this);
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
		SaleInputHandler.requestFocus(lineInput);
	}

	public boolean isLineInputFocused() {
		return SaleInputHandler.isLineInputFocused(lineInput);
	}

	public String getLineInputText() {
		return lineInput != null ? lineInput.getText() : "";
	}

	public void clearLineInput() {
		if (lineInput != null) {
			lineInput.clear();
		}
	}

	public boolean isItemTableFocused() {
		return SaleInputHandler.isItemTableFocused(itemTable);
	}

	public void focusItemTable() {
		SaleInputHandler.focusItemTable(itemTable);
	}

	public void appendLineInput(String text) {
		SaleInputHandler.appendLineInput(lineInput, text);
	}

	public void handleLeftArrow() {
		SaleInputHandler.handleLeftArrow(lineInput);
	}

	public void handleRightArrow() {
		SaleInputHandler.handleRightArrow(lineInput);
	}

	public void handleBackspace() {
		SaleInputHandler.handleBackspace(lineInput);
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

		Trx newTrx = saleService.startNewTransaction();

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
		if (itemTable != null) {
			itemTable.getItems().clear();
		}
		updateCustDisplay();
		logger.info("New transaction started");
	}

	public void handleItemSearch(jfxpos.models.InputSearchMode searchMode, String searchText) {
		try {
			Item chosenItem = null;

			if (searchMode == jfxpos.models.InputSearchMode.BARCODE) {
				chosenItem = saleService.findItemByBarcode(searchText);
			} else if (searchMode == jfxpos.models.InputSearchMode.ART) {
				java.util.List<Item> candidates = saleService.findItemsByArticle(searchText);
				if (candidates == null || candidates.isEmpty()) {
					jfxpos.util.SoundUtil.playAlert();
					if (itemDescriptionLabel != null) {
						itemDescriptionLabel.setText("ITEM NOT FOUND");
					}
					if (itemPriceLabel != null) {
						itemPriceLabel.setText("");
					}
					clearLineInput();
					return;
				}

				if (candidates.size() == 1) {
					chosenItem = candidates.get(0);
				} else {
					// Extract unique colors
					java.util.List<Item> uniqueColorItems = new java.util.ArrayList<>();
					java.util.Set<String> colorsSeen = new java.util.HashSet<>();
					for (Item candidate : candidates) {
						String col = candidate.getItemCol();
						if (col == null)
							col = "";
						if (colorsSeen.add(col.trim().toUpperCase())) {
							uniqueColorItems.add(candidate);
						}
					}

					java.util.List<Item> filteredByColor = candidates;
					if (uniqueColorItems.size() > 1) {
						SelectColDialog colDialog = new SelectColDialog(getCurrentWindow(), uniqueColorItems);
						colDialog.openDialog();
						Item selectedColorItem = colDialog.getSelectedItem();
						if (selectedColorItem == null) {
							logger.info("Search item cancelled during color selection");
							return; // User cancelled
						}

						String selectedColor = selectedColorItem.getItemCol();
						filteredByColor = new java.util.ArrayList<>();
						for (Item candidate : candidates) {
							if (selectedColor == null && candidate.getItemCol() == null) {
								filteredByColor.add(candidate);
							} else if (selectedColor != null
									&& selectedColor.equalsIgnoreCase(candidate.getItemCol())) {
								filteredByColor.add(candidate);
							}
						}
					}

					// Extract unique sizes from filteredByColor
					java.util.List<Item> uniqueSizeItems = new java.util.ArrayList<>();
					java.util.Set<String> sizesSeen = new java.util.HashSet<>();
					for (Item candidate : filteredByColor) {
						String sz = candidate.getItemSize();
						if (sz == null)
							sz = "";
						if (sizesSeen.add(sz.trim().toUpperCase())) {
							uniqueSizeItems.add(candidate);
						}
					}

					if (uniqueSizeItems.size() > 1) {
						SelectSizeDialog sizeDialog = new SelectSizeDialog(getCurrentWindow(), filteredByColor);
						sizeDialog.openDialog();
						chosenItem = sizeDialog.getSelectedItem();
						if (chosenItem == null) {
							logger.info("Search item cancelled during size selection");
							return; // User cancelled
						}
					} else if (!filteredByColor.isEmpty()) {
						chosenItem = filteredByColor.get(0);
					}
				}
			}

			if (chosenItem != null) {
				TrxItem lineItem = saleService.createTrxItemFromItem(chosenItem);
				if (lineItem != null) {
					Trx activeTrx = currentTrx.get();
					if (activeTrx != null) {
						activeTrx.getItems().add(lineItem);

						// Update TableView
						if (itemTable != null) {
							itemTable.getItems().setAll(activeTrx.getItems());
						}

						// Recalculate transaction totals
						int newQty = activeTrx.getQty() + lineItem.getQty();
						activeTrx.setQty(newQty);

						BigDecimal newGrandTotal = activeTrx.getGrandTotal().add(lineItem.getGrandTotal());
						activeTrx.setGrandTotal(newGrandTotal);

						// Update description and price labels
						if (itemDescriptionLabel != null) {
							itemDescriptionLabel.setText(lineItem.getItemDescr());
						}
						if (itemPriceLabel != null) {
							itemPriceLabel.setText(String.format("%,.0f", lineItem.getItemPrice()));
						}

						// Update customer display
						updateCustDisplay();
					}
					clearLineInput();
					logger.info("Item added to transaction: " + lineItem.getItemDescr());
				}
			} else {
				jfxpos.util.SoundUtil.playAlert();
				if (itemDescriptionLabel != null) {
					itemDescriptionLabel.setText("ITEM NOT FOUND");
				}
				if (itemPriceLabel != null) {
					itemPriceLabel.setText("");
				}
				clearLineInput();
			}
		} catch (Exception e) {
			logger.severe("Error searching item: " + e.getMessage());
			MessageBox.error(getCurrentWindow(), "Error: " + e.getMessage());
		}
	}

	private void openChannelDialog() {
		SaleDialogManager.openChannelDialog(getCurrentWindow(), currentTrx.get());
	}

	private void openCustRegisterDialog() {
		SaleDialogManager.openCustRegisterDialog(getCurrentWindow(), currentTrx.get());
	}

	private void openCustSearchDialog() {
		SaleDialogManager.openCustSearchDialog(getCurrentWindow(), currentTrx.get());
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
						"Konfirmasi Hapus Customer",
						ButtonType.CANCEL);
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
		SaleDialogManager.openPromoItemDialog(getCurrentWindow(), currentTrx.get());
	}

	private void openPromoPaymentDialog() {
		SaleDialogManager.openPromoPaymentDialog(getCurrentWindow(), currentTrx.get());
	}

	private void openPromoNextTxDialog() {
		SaleDialogManager.openPromoNextTxDialog(getCurrentWindow(), currentTrx.get());
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

			String customerName = (trx != null) ? trx.getCustomerName() : "";
			String displayName = (customerName == null || "NONE".equalsIgnoreCase(customerName.trim())) ? ""
					: customerName;
			custDisplay.setCustomerName(displayName);

			if (trx != null && trx.getItems() != null) {
				custDisplay.setItems(trx.getItems());
			} else {
				custDisplay.setItems(new java.util.ArrayList<>());
			}
		}
	}

	private CustDisplayController getCustDisplayController() {
		CustDisplayWindow window = CustDisplayWindow.getInstance();
		return window != null ? window.getController() : null;
	}
}
