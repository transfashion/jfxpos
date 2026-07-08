package jfxpos.models;

import jfxpos.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrxItem extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "TRX_ITEM";

		public static final class Columns {
			public static final String ID = "TRX_ITEM_ID";
			public static final String TRX_ID = "TRX_ID";
			public static final String ITEM_ID = "ITEM_ID";
			public static final String BARCODE = "BARCODE";
			public static final String ITEM_ART = "ITEM_ART";
			public static final String ITEM_COL = "ITEM_COL";
			public static final String ITEM_SIZE = "ITEM_SIZE";
			public static final String ITEM_DESCR = "ITEM_DESCR";
			public static final String ITEM_PRICEGROSS = "ITEM_PRICEGROSS";
			public static final String ITEM_PRICE = "ITEM_PRICE";
			public static final String ITEM_DISC = "ITEM_DISC";
			public static final String ITEM_IS_SPECIALPRICE = "ITEM_IS_SPECIALPRICE";
			public static final String ITEM_IS_CONSUMABLE = "ITEM_IS_CONSUMABLE";
			public static final String ITEM_IS_DONATION = "ITEM_IS_DONATION";
			public static final String ITEM_IS_DELIVERY = "ITEM_IS_DELIVERY";
			public static final String PROMOITEM_ID = "PROMOITEM_ID";
			public static final String PROMOITEM_NOTE = "PROMOITEM_NOTE";
			public static final String QTY = "QTY";
			public static final String PRICEGROSS = "PRICEGROSS";
			public static final String DISCVALUE = "DISCVALUE";
			public static final String PRICENETT = "PRICENETT";
			public static final String SUBTOTAL_GROSS = "SUBTOTAL_GROSS";
			public static final String SUBTOTAL_DISCOUNT = "SUBTOTAL_DISCOUNT";
			public static final String SUBTOTAL_NETT = "SUBTOTAL_NETT";
			public static final String PROMOPAYM_ID = "PROMOPAYM_ID";
			public static final String PROMOPAYM_NOTE = "PROMOPAYM_NOTE";
			public static final String PROMOPAYM_VALUE = "PROMOPAYM_VALUE";
			public static final String TOTAL = "TOTAL";
			public static final String TOTAL_FP = "TOTAL_FP";
			public static final String TOTAL_MD = "TOTAL_MD";
			public static final String IS_INCLUDETAX = "IS_INCLUDETAX";
			public static final String SALES_GROSS = "SALES_GROSS";
			public static final String TAX_PERCENT = "TAX_PERCENT";
			public static final String TAX_VALUE = "TAX_VALUE";
			public static final String SALES_NETT = "SALES_NETT";
			public static final String DONASI = "DONASI";
			public static final String DELIVERY = "DELIVERY";
			public static final String GRANDTOTAL = "GRANDTOTAL";
			public static final String CTG_ID = "CTG_ID";
			public static final String UNIT_ID = "UNIT_ID";
			public static final String STRUCT_ID = "STRUCT_ID";
			public static final String BRAND_ID = "BRAND_ID";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private long id;
	private long trxId;
	private long itemId;
	private String barcode;
	private String itemArt;
	private String itemCol;
	private String itemSize;
	private String itemDescr;
	private BigDecimal itemPriceGross = BigDecimal.ZERO;
	private BigDecimal itemPrice = BigDecimal.ZERO;
	private BigDecimal itemDisc = BigDecimal.ZERO;
	private boolean itemIsSpecialPrice;
	private boolean itemIsConsumable = true;
	private boolean itemIsDonation = true;
	private boolean itemIsDelivery = true;
	private Integer promoItemId;
	private String promoItemNote;
	private int qty;
	private BigDecimal priceGross = BigDecimal.ZERO;
	private BigDecimal discValue = BigDecimal.ZERO;
	private BigDecimal priceNett = BigDecimal.ZERO;
	private BigDecimal subtotalGross = BigDecimal.ZERO;
	private BigDecimal subtotalDiscount = BigDecimal.ZERO;
	private BigDecimal subtotalNett = BigDecimal.ZERO;
	private Integer promoPaymId;
	private String promoPaymNote;
	private BigDecimal promoPaymValue = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal totalFp = BigDecimal.ZERO;
	private BigDecimal totalMd = BigDecimal.ZERO;
	private boolean isIncludeTax = true;
	private BigDecimal salesGross = BigDecimal.ZERO;
	private BigDecimal taxPercent = BigDecimal.ZERO;
	private BigDecimal taxValue = BigDecimal.ZERO;
	private BigDecimal salesNett = BigDecimal.ZERO;
	private BigDecimal donasi = BigDecimal.ZERO;
	private BigDecimal delivery = BigDecimal.ZERO;
	private BigDecimal grandTotal = BigDecimal.ZERO;
	private Integer ctgId;
	private Integer unitId;
	private Integer structId;
	private Integer brandId;
	private LocalDateTime dataTimestamp;

	public TrxItem() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTrxId() {
		return trxId;
	}

	public void setTrxId(long trxId) {
		this.trxId = trxId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getItemArt() {
		return itemArt;
	}

	public void setItemArt(String itemArt) {
		this.itemArt = itemArt;
	}

	public String getItemCol() {
		return itemCol;
	}

	public void setItemCol(String itemCol) {
		this.itemCol = itemCol;
	}

	public String getItemSize() {
		return itemSize;
	}

	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	public String getItemDescr() {
		return itemDescr;
	}

	public void setItemDescr(String itemDescr) {
		this.itemDescr = itemDescr;
	}

	public BigDecimal getItemPriceGross() {
		return itemPriceGross;
	}

	public void setItemPriceGross(BigDecimal itemPriceGross) {
		this.itemPriceGross = itemPriceGross;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemDisc() {
		return itemDisc;
	}

	public void setItemDisc(BigDecimal itemDisc) {
		this.itemDisc = itemDisc;
	}

	public boolean isItemIsSpecialPrice() {
		return itemIsSpecialPrice;
	}

	public void setItemIsSpecialPrice(boolean itemIsSpecialPrice) {
		this.itemIsSpecialPrice = itemIsSpecialPrice;
	}

	public boolean isItemIsConsumable() {
		return itemIsConsumable;
	}

	public void setItemIsConsumable(boolean itemIsConsumable) {
		this.itemIsConsumable = itemIsConsumable;
	}

	public boolean isItemIsDonation() {
		return itemIsDonation;
	}

	public void setItemIsDonation(boolean itemIsDonation) {
		this.itemIsDonation = itemIsDonation;
	}

	public boolean isItemIsDelivery() {
		return itemIsDelivery;
	}

	public void setItemIsDelivery(boolean itemIsDelivery) {
		this.itemIsDelivery = itemIsDelivery;
	}

	public Integer getPromoItemId() {
		return promoItemId;
	}

	public void setPromoItemId(Integer promoItemId) {
		this.promoItemId = promoItemId;
	}

	public String getPromoItemNote() {
		return promoItemNote;
	}

	public void setPromoItemNote(String promoItemNote) {
		this.promoItemNote = promoItemNote;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public BigDecimal getPriceGross() {
		return priceGross;
	}

	public void setPriceGross(BigDecimal priceGross) {
		this.priceGross = priceGross;
	}

	public BigDecimal getDiscValue() {
		return discValue;
	}

	public void setDiscValue(BigDecimal discValue) {
		this.discValue = discValue;
	}

	public BigDecimal getPriceNett() {
		return priceNett;
	}

	public void setPriceNett(BigDecimal priceNett) {
		this.priceNett = priceNett;
	}

	public BigDecimal getSubtotalGross() {
		return subtotalGross;
	}

	public void setSubtotalGross(BigDecimal subtotalGross) {
		this.subtotalGross = subtotalGross;
	}

	public BigDecimal getSubtotalDiscount() {
		return subtotalDiscount;
	}

	public void setSubtotalDiscount(BigDecimal subtotalDiscount) {
		this.subtotalDiscount = subtotalDiscount;
	}

	public BigDecimal getSubtotalNett() {
		return subtotalNett;
	}

	public void setSubtotalNett(BigDecimal subtotalNett) {
		this.subtotalNett = subtotalNett;
	}

	public Integer getPromoPaymId() {
		return promoPaymId;
	}

	public void setPromoPaymId(Integer promoPaymId) {
		this.promoPaymId = promoPaymId;
	}

	public String getPromoPaymNote() {
		return promoPaymNote;
	}

	public void setPromoPaymNote(String promoPaymNote) {
		this.promoPaymNote = promoPaymNote;
	}

	public BigDecimal getPromoPaymValue() {
		return promoPaymValue;
	}

	public void setPromoPaymValue(BigDecimal promoPaymValue) {
		this.promoPaymValue = promoPaymValue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalFp() {
		return totalFp;
	}

	public void setTotalFp(BigDecimal totalFp) {
		this.totalFp = totalFp;
	}

	public BigDecimal getTotalMd() {
		return totalMd;
	}

	public void setTotalMd(BigDecimal totalMd) {
		this.totalMd = totalMd;
	}

	public boolean isIncludeTax() {
		return isIncludeTax;
	}

	public void setIncludeTax(boolean includeTax) {
		isIncludeTax = includeTax;
	}

	public BigDecimal getSalesGross() {
		return salesGross;
	}

	public void setSalesGross(BigDecimal salesGross) {
		this.salesGross = salesGross;
	}

	public BigDecimal getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}

	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	public BigDecimal getSalesNett() {
		return salesNett;
	}

	public void setSalesNett(BigDecimal salesNett) {
		this.salesNett = salesNett;
	}

	public BigDecimal getDonasi() {
		return donasi;
	}

	public void setDonasi(BigDecimal donasi) {
		this.donasi = donasi;
	}

	public BigDecimal getDelivery() {
		return delivery;
	}

	public void setDelivery(BigDecimal delivery) {
		this.delivery = delivery;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Integer getCtgId() {
		return ctgId;
	}

	public void setCtgId(Integer ctgId) {
		this.ctgId = ctgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getStructId() {
		return structId;
	}

	public void setStructId(Integer structId) {
		this.structId = structId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}
}
