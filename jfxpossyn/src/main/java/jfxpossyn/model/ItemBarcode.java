package jfxpossyn.model;

import java.time.LocalDateTime;

public class ItemBarcode extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "ITEMBARCODE";

		public static final class Columns {
			public static final String ITEMBARCODE_ID = "ITEMBARCODE_ID";
			public static final String ITEM_ID = "ITEM_ID";
			public static final String BARCODE = "BARCODE";
			public static final String BRAND_ID = "BRAND_ID";
			public static final String ITEMBARCODE_ISDISABLED = "ITEMBARCODE_ISDISABLED";
			public static final String CREATED_AT = "CREATED_AT";
			public static final String MODIFIED_AT = "MODIFIED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private long itemBarcodeId;
	private long itemId;
	private String barcode;
	private Integer brandId;
	private boolean itembarcodeIsDisabled = false;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private LocalDateTime dataTimestamp;

	public long getItemBarcodeId() {
		return itemBarcodeId;
	}

	public void setItemBarcodeId(long itemBarcodeId) {
		this.itemBarcodeId = itemBarcodeId;
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

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public boolean isItembarcodeIsDisabled() {
		return itembarcodeIsDisabled;
	}

	public void setItembarcodeIsDisabled(boolean itembarcodeIsDisabled) {
		this.itembarcodeIsDisabled = itembarcodeIsDisabled;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}
}
