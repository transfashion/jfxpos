package jfxpos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import jfxpos.models.TrxItem;
import java.math.BigDecimal;

public class ItemDescrCell extends TableCell<TrxItem, TrxItem> {
	private VBox root;

	@FXML
	private Label itemArtLabel;

	@FXML
	private Label itemColLabel;

	@FXML
	private Label itemSizeLabel;

	@FXML
	private Label itemIdLabel;

	@FXML
	private Label itemDescrLabel;

	@FXML
	private Label itemPriceLabel;

	@FXML
	private Label itemDiscLabel;

	@FXML
	private Label itemPromoLabel;

	@FXML
	private Label itemPriceNettLabel;

	public ItemDescrCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/itemdescrcell.fxml"));
			loader.setController(this);
			root = loader.load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(TrxItem item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setGraphic(null);
		} else {
			itemArtLabel.setText(item.getItemArt() != null ? item.getItemArt() : "");
			itemColLabel.setText(item.getItemCol() != null ? item.getItemCol() : "");
			itemSizeLabel.setText(item.getItemSize() != null ? item.getItemSize() : "");
			itemIdLabel.setText(String.valueOf(item.getItemId()));
			itemDescrLabel.setText(item.getItemDescr() != null ? item.getItemDescr() : "");

			BigDecimal price = item.getItemPrice();
			BigDecimal net = item.getPriceNett();
			boolean showPrice = price != null && net != null && price.compareTo(net) != 0;
			itemPriceLabel.setText(price != null ? String.format("%,.0f", price) : "0");
			itemPriceLabel.setVisible(showPrice);
			itemPriceLabel.setManaged(showPrice);

			BigDecimal disc = item.getDiscValue();
			boolean hasDisc = disc != null && disc.compareTo(BigDecimal.ZERO) > 0;
			itemDiscLabel.setText(hasDisc ? "Disc: " + String.format("%,.0f", disc) : "");
			itemDiscLabel.setVisible(hasDisc);
			itemDiscLabel.setManaged(hasDisc);

			String promo = item.getPromoItemNote();
			boolean hasPromo = promo != null && !promo.trim().isEmpty();
			itemPromoLabel.setText(hasPromo ? promo : "");
			itemPromoLabel.setVisible(hasPromo);
			itemPromoLabel.setManaged(hasPromo);

			itemPriceNettLabel.setText(net != null ? String.format("%,.0f", net) : "0");

			setGraphic(root);
		}
	}
}
