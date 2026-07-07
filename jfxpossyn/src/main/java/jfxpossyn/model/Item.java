package jfxpossyn.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Item extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "ITEM";

		public static final class Columns {
			public static final String ITEM_ID = "ITEM_ID";
			public static final String ITEM_ART = "ITEM_ART";
			public static final String ITEM_COL = "ITEM_COL";
			public static final String ITEM_SIZE = "ITEM_SIZE";
			public static final String ITEM_DESCR = "ITEM_DESCR";

			public static final String ITEM_PRICEGROSS = "ITEM_PRICEGROSS";
			public static final String ITEM_PRICE = "ITEM_PRICE";
			public static final String ITEM_DISC = "ITEM_DISC";
			public static final String ITEM_ISSPECIALPRICE = "ITEM_ISSPECIALPRICE";

			public static final String ITEM_ISDISABLED = "ITEM_ISDISABLED";

			public static final String CTG_ID = "CTG_ID";
			public static final String CTG_NAME = "CTG_NAME";
			public static final String UNIT_ID = "UNIT_ID";
			public static final String UNIT_NAME = "UNIT_NAME";
			public static final String STRUCT_ID = "STRUCT_ID";
			public static final String STRUCT_NAME = "STRUCT_NAME";
			public static final String BRAND_ID = "BRAND_ID";
			public static final String BRAND_NAME = "BRAND_NAME";

			public static final String CREATED_AT = "CREATED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
			public static final String MD5HASH = "MD5HASH";
		}
	}

	private long itemId;
	private String itemArt;
	private String itemCol;
	private String itemSize;
	private String itemDescr;
	private BigDecimal itemPriceGross = BigDecimal.ZERO;
	private BigDecimal itemPrice = BigDecimal.ZERO;
	private BigDecimal itemDisc = BigDecimal.ZERO;
	private boolean itemIsSpecialPrice;
	private boolean itemIsDisabled;
	private Integer ctgId;
	private String ctgName;
	private Integer unitId;
	private String unitName;
	private Integer structId;
	private String structName;
	private Integer brandId;
	private String brandName;
	private LocalDateTime createdAt;
	private LocalDateTime dataTimestamp;
	private String md5Hash;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
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

	public boolean isItemIsDisabled() {
		return itemIsDisabled;
	}

	public void setItemIsDisabled(boolean itemIsDisabled) {
		this.itemIsDisabled = itemIsDisabled;
	}

	public Integer getCtgId() {
		return ctgId;
	}

	public void setCtgId(Integer ctgId) {
		this.ctgId = ctgId;
	}

	public String getCtgName() {
		return ctgName;
	}

	public void setCtgName(String ctgName) {
		this.ctgName = ctgName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getStructId() {
		return structId;
	}

	public void setStructId(Integer structId) {
		this.structId = structId;
	}

	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}
}
