package jfxpos.models;

import jfxpos.Model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trx extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "TRX";

		public static final class Columns {
			public static final String ID = "TRX_ID";
			public static final String TRX_DOC = "TRX_DOC";
			public static final String TRX_DATE = "TRX_DATE";
			public static final String TRX_TIME = "TRX_TIME";
			public static final String TOTAL_QTY = "TOTAL_QTY";
			public static final String SUBTOTAL = "SUBTOTAL";
			public static final String TOTAL_DISCOUNT = "TOTAL_DISCOUNT";
			public static final String TOTAL_TAX = "TOTAL_TAX";
			public static final String GRAND_TOTAL = "GRAND_TOTAL";
			public static final String TOTAL_PAID = "TOTAL_PAID";
			public static final String TOTAL_RETURN = "TOTAL_RETURN";
			public static final String CHANNEL_ID = "CHANNEL_ID";
			public static final String CASHIER_ID = "CASHIER_ID";
			public static final String SITE_ID = "SITE_ID";
			public static final String UNIT_ID = "UNIT_ID";
			public static final String STRUCT_ID = "STRUCT_ID";
			public static final String IS_ACTIVE = "IS_ACTIVE";
			public static final String CREATED_AT = "CREATED_AT";
			public static final String MODIFIED_AT = "MODIFIED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private final LongProperty id = new SimpleLongProperty(this, "id");
	private final StringProperty trxDoc = new SimpleStringProperty(this, "trxDoc");
	private final ObjectProperty<LocalDate> trxDate = new SimpleObjectProperty<>(this, "trxDate");
	private final ObjectProperty<LocalTime> trxTime = new SimpleObjectProperty<>(this, "trxTime");
	private final IntegerProperty totalQty = new SimpleIntegerProperty(this, "totalQty");
	private final ObjectProperty<BigDecimal> subtotal = new SimpleObjectProperty<>(this, "subtotal", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalDiscount = new SimpleObjectProperty<>(this, "totalDiscount",
			BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalTax = new SimpleObjectProperty<>(this, "totalTax", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> grandTotal = new SimpleObjectProperty<>(this, "grandTotal",
			BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalPaid = new SimpleObjectProperty<>(this, "totalPaid", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalReturn = new SimpleObjectProperty<>(this, "totalReturn",
			BigDecimal.ZERO);
	private final IntegerProperty channelId = new SimpleIntegerProperty(this, "channelId");
	private final StringProperty channelName = new SimpleStringProperty(this, "channelName");

	private final IntegerProperty cashierId = new SimpleIntegerProperty(this, "cashierId");
	private final IntegerProperty siteId = new SimpleIntegerProperty(this, "siteId");
	private final IntegerProperty unitId = new SimpleIntegerProperty(this, "unitId");
	private final IntegerProperty structId = new SimpleIntegerProperty(this, "structId");
	private final BooleanProperty isActive = new SimpleBooleanProperty(this, "isActive", true);
	private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>(this, "createdAt");
	private final ObjectProperty<LocalDateTime> modifiedAt = new SimpleObjectProperty<>(this, "modifiedAt");
	private final ObjectProperty<LocalDateTime> dataTimestamp = new SimpleObjectProperty<>(this, "dataTimestamp");

	// Relationships
	private List<TrxItem> items = new ArrayList<>();
	private List<TrxPaym> payments = new ArrayList<>();

	public Trx() {
	}

	public long getId() {
		return id.get();
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public LongProperty idProperty() {
		return id;
	}

	public String getTrxDoc() {
		return trxDoc.get();
	}

	public void setTrxDoc(String trxDoc) {
		this.trxDoc.set(trxDoc);
	}

	public StringProperty trxDocProperty() {
		return trxDoc;
	}

	public LocalDate getTrxDate() {
		return trxDate.get();
	}

	public void setTrxDate(LocalDate trxDate) {
		this.trxDate.set(trxDate);
	}

	public ObjectProperty<LocalDate> trxDateProperty() {
		return trxDate;
	}

	public LocalTime getTrxTime() {
		return trxTime.get();
	}

	public void setTrxTime(LocalTime trxTime) {
		this.trxTime.set(trxTime);
	}

	public ObjectProperty<LocalTime> trxTimeProperty() {
		return trxTime;
	}

	public int getTotalQty() {
		return totalQty.get();
	}

	public void setTotalQty(int totalQty) {
		this.totalQty.set(totalQty);
	}

	public IntegerProperty totalQtyProperty() {
		return totalQty;
	}

	public BigDecimal getSubtotal() {
		return subtotal.get();
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal.set(subtotal);
	}

	public ObjectProperty<BigDecimal> subtotalProperty() {
		return subtotal;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount.get();
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount.set(totalDiscount);
	}

	public ObjectProperty<BigDecimal> totalDiscountProperty() {
		return totalDiscount;
	}

	public BigDecimal getTotalTax() {
		return totalTax.get();
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax.set(totalTax);
	}

	public ObjectProperty<BigDecimal> totalTaxProperty() {
		return totalTax;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal.get();
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal.set(grandTotal);
	}

	public ObjectProperty<BigDecimal> grandTotalProperty() {
		return grandTotal;
	}

	public BigDecimal getTotalPaid() {
		return totalPaid.get();
	}

	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid.set(totalPaid);
	}

	public ObjectProperty<BigDecimal> totalPaidProperty() {
		return totalPaid;
	}

	public BigDecimal getTotalReturn() {
		return totalReturn.get();
	}

	public void setTotalReturn(BigDecimal totalReturn) {
		this.totalReturn.set(totalReturn);
	}

	public ObjectProperty<BigDecimal> totalReturnProperty() {
		return totalReturn;
	}

	public Integer getChannelId() {
		return channelId.get();
	}

	public void setChannelId(Integer channelId) {
		this.channelId.set(channelId != null ? channelId : 0);
	}

	public IntegerProperty channelIdProperty() {
		return channelId;
	}

	public String getChannelName() {
		return channelName.get();
	}

	public void setChannelName(String channelName) {
		this.channelName.set(channelName);
	}

	public StringProperty channelNameProperty() {
		return channelName;
	}

	public Integer getCashierId() {
		return cashierId.get();
	}

	public void setCashierId(Integer cashierId) {
		this.cashierId.set(cashierId != null ? cashierId : 0);
	}

	public IntegerProperty cashierIdProperty() {
		return cashierId;
	}

	public Integer getSiteId() {
		return siteId.get();
	}

	public void setSiteId(Integer siteId) {
		this.siteId.set(siteId != null ? siteId : 0);
	}

	public IntegerProperty siteIdProperty() {
		return siteId;
	}

	public Integer getUnitId() {
		return unitId.get();
	}

	public void setUnitId(Integer unitId) {
		this.unitId.set(unitId != null ? unitId : 0);
	}

	public IntegerProperty unitIdProperty() {
		return unitId;
	}

	public Integer getStructId() {
		return structId.get();
	}

	public void setStructId(Integer structId) {
		this.structId.set(structId != null ? structId : 0);
	}

	public IntegerProperty structIdProperty() {
		return structId;
	}

	public boolean isActive() {
		return isActive.get();
	}

	public void setActive(boolean active) {
		this.isActive.set(active);
	}

	public BooleanProperty isActiveProperty() {
		return isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt.get();
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt.set(createdAt);
	}

	public ObjectProperty<LocalDateTime> createdAtProperty() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt.get();
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt.set(modifiedAt);
	}

	public ObjectProperty<LocalDateTime> modifiedAtProperty() {
		return modifiedAt;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp.get();
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp.set(dataTimestamp);
	}

	public ObjectProperty<LocalDateTime> dataTimestampProperty() {
		return dataTimestamp;
	}

	public List<TrxItem> getItems() {
		return items;
	}

	public void setItems(List<TrxItem> items) {
		this.items = items;
	}

	public List<TrxPaym> getPayments() {
		return payments;
	}

	public void setPayments(List<TrxPaym> payments) {
		this.payments = payments;
	}
}
