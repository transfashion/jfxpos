package jfxpos.models;

import jfxpos.Model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
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
			public static final String CUSTOMER_ID = "CUSTOMER_ID";
			public static final String CUSTOMER_DISCOUNT = "CUSTOMER_DISCOUNT";
			public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
			public static final String CUSTOMERTYPE_ID = "CUSTOMERTYPE_ID";
			public static final String CUSTOMER_GENDER = "CUSTOMER_GENDER";
			public static final String CUSTOMER_BIRTHDATE = "CUSTOMER_BIRTHDATE";
			public static final String QTY = "QTY";
			public static final String SUBTOTAL_GROSS = "SUBTOTAL_GROSS";
			public static final String SUBTOTAL_DISCOUNT = "SUBTOTAL_DISCOUNT";
			public static final String SUBTOTAL_NETT = "SUBTOTAL_NETT";
			public static final String PROMOPAYM_VALUE = "PROMOPAYM_VALUE";
			public static final String TOTAL = "TOTAL";
			public static final String TOTAL_FP = "TOTAL_FP";
			public static final String TOTAL_MD = "TOTAL_MD";
			public static final String TOTAL_PAID = "TOTAL_PAID";
			public static final String TOTAL_RETURN = "TOTAL_RETURN";
			public static final String SALES_GROSS = "SALES_GROSS";
			public static final String TAX_VALUE = "TAX_VALUE";
			public static final String SALES_NETT = "SALES_NETT";
			public static final String DONASI = "DONASI";
			public static final String DELIVERY = "DELIVERY";
			public static final String GRANDTOTAL = "GRANDTOTAL";
			public static final String CHANNEL_ID = "CHANNEL_ID";
			public static final String CASHIER_ID = "CASHIER_ID";
			public static final String SITE_ID = "SITE_ID";
			public static final String UNIT_ID = "UNIT_ID";
			public static final String STRUCT_ID = "STRUCT_ID";
			public static final String IS_ACTIVE = "IS_ACTIVE";
			public static final String CREATED_AT = "CREATED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";

			public static final String PROMOITEM_ID = "PROMOITEM_ID";
			public static final String PROMOITEM_CODE = "PROMOITEM_CODE";
			public static final String PROMOITEM_NAME = "PROMOITEM_NAME";
			public static final String PROMOITEM_DESCR = "PROMOITEM_DESCR";

			public static final String PROMOPAYM_ID = "PROMOPAYM_ID";
			public static final String PROMOPAYM_CODE = "PROMOPAYM_CODE";
			public static final String PROMOPAYM_NAME = "PROMOPAYM_NAME";
			public static final String PROMOPAYM_DESCR = "PROMOPAYM_DESCR";

			public static final String PROMONEXTTX_ID = "PROMONEXTTX_ID";
			public static final String PROMONEXTTX_CODE = "PROMONEXTTX_CODE";
			public static final String PROMONEXTTX_NAME = "PROMONEXTTX_NAME";
			public static final String PROMONEXTTX_DESCR = "PROMONEXTTX_DESCR";

			public static final String PROMOITEM_COUNT = "PROMOITEM_COUNT";
			public static final String PROMOPAYM_COUNT = "PROMOPAYM_COUNT";
			public static final String PROMONEXTTX_COUNT = "PROMONEXTTX_COUNT";
		}
	}

	private final LongProperty id = new SimpleLongProperty(this, "id");
	private final StringProperty trxDoc = new SimpleStringProperty(this, "trxDoc");
	private final ObjectProperty<LocalDate> trxDate = new SimpleObjectProperty<>(this, "trxDate");
	private final ObjectProperty<LocalTime> trxTime = new SimpleObjectProperty<>(this, "trxTime");
	private final IntegerProperty qty = new SimpleIntegerProperty(this, "qty");
	private final ObjectProperty<BigDecimal> subtotal = new SimpleObjectProperty<>(this, "subtotal", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalDiscount = new SimpleObjectProperty<>(this, "totalDiscount",
			BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalTax = new SimpleObjectProperty<>(this, "totalTax", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> grandTotal = new SimpleObjectProperty<>(this, "grandTotal",
			BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalPaid = new SimpleObjectProperty<>(this, "totalPaid", BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> totalReturn = new SimpleObjectProperty<>(this, "totalReturn",
			BigDecimal.ZERO);
	private final IntegerProperty channelId = new SimpleIntegerProperty(this, "channelId", 0);
	private final StringProperty channelName = new SimpleStringProperty(this, "channelName", "NONE");
	private final LongProperty customerId = new SimpleLongProperty(this, "customerId");
	private final StringProperty customerName = new SimpleStringProperty(this, "customerName", "NONE");
	private final IntegerProperty customerTypeId = new SimpleIntegerProperty(this, "customerTypeId");
	private final StringProperty customerTypeName = new SimpleStringProperty(this, "customerTypeName", "");
	private final IntegerProperty customerGender = new SimpleIntegerProperty(this, "customerGender", 0);
	private final ObjectProperty<LocalDate> customerBirthdate = new SimpleObjectProperty<>(this, "customerBirthdate");

	private final ObjectProperty<BigDecimal> customerDiscount = new SimpleObjectProperty<>(this, "customerDiscount",
			BigDecimal.ZERO);

	private final LongProperty promoItemId = new SimpleLongProperty(this, "promoItemId");
	private final StringProperty promoItemCode = new SimpleStringProperty(this, "promoItemCode", "");
	private final StringProperty promoItemName = new SimpleStringProperty(this, "promoItemName", "");
	private final StringProperty promoItemDescr = new SimpleStringProperty(this, "promoItemDescr", "");

	private final LongProperty promoPaymId = new SimpleLongProperty(this, "promoPaymId");
	private final StringProperty promoPaymCode = new SimpleStringProperty(this, "promoPaymCode", "");
	private final StringProperty promoPaymName = new SimpleStringProperty(this, "promoPaymName", "");
	private final StringProperty promoPaymDescr = new SimpleStringProperty(this, "promoPaymDescr", "");

	private final LongProperty promoNextTxId = new SimpleLongProperty(this, "promoNextTxId");
	private final StringProperty promoNextTxCode = new SimpleStringProperty(this, "promoNextTxCode", "");
	private final StringProperty promoNextTxName = new SimpleStringProperty(this, "promoNextTxName", "");
	private final StringProperty promoNextTxDescr = new SimpleStringProperty(this, "promoNextTxDescr", "");

	private final IntegerProperty promoItemCount = new SimpleIntegerProperty(this, "promoItemCount", 0);
	private final IntegerProperty promoPaymCount = new SimpleIntegerProperty(this, "promoPaymCount", 0);
	private final IntegerProperty promoNextTxCount = new SimpleIntegerProperty(this, "promoNextTxCount", 0);

	private final IntegerProperty cashierId = new SimpleIntegerProperty(this, "cashierId");
	private final IntegerProperty siteId = new SimpleIntegerProperty(this, "siteId");
	private final IntegerProperty unitId = new SimpleIntegerProperty(this, "unitId");
	private final IntegerProperty structId = new SimpleIntegerProperty(this, "structId");

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

	public int getQty() {
		return qty.get();
	}

	public void setQty(int qty) {
		this.qty.set(qty);
	}

	public IntegerProperty qtyProperty() {
		return qty;
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

	public Long getCustomerId() {
		return customerId.get();
	}

	public void setCustomerId(Long customerId) {
		this.customerId.set(customerId != null ? customerId : 0L);
	}

	public LongProperty customerIdProperty() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName.get();
	}

	public void setCustomerName(String customerName) {
		this.customerName.set(customerName);
	}

	public StringProperty customerNameProperty() {
		return customerName;
	}

	public Integer getCustomerGender() {
		return customerGender.get();
	}

	public void setCustomerGender(Integer customerGender) {
		this.customerGender.set(customerGender != null ? customerGender : 0);
	}

	public IntegerProperty customerGenderProperty() {
		return customerGender;
	}

	public LocalDate getCustomerBirthdate() {
		return customerBirthdate.get();
	}

	public void setCustomerBirthdate(LocalDate customerBirthdate) {
		this.customerBirthdate.set(customerBirthdate);
	}

	public ObjectProperty<LocalDate> customerBirthdateProperty() {
		return customerBirthdate;
	}

	public Integer getCustomerTypeId() {
		return customerTypeId.get();
	}

	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId.set(customerTypeId != null ? customerTypeId : 0);
	}

	public IntegerProperty customerTypeIdProperty() {
		return customerTypeId;
	}

	public String getCustomerTypeName() {
		return customerTypeName.get();
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName.set(customerTypeName);
	}

	public StringProperty customerTypeNameProperty() {
		return customerTypeName;
	}

	public BigDecimal getCustomerDiscount() {
		return customerDiscount.get();
	}

	public void setCustomerDiscount(BigDecimal customerDiscount) {
		this.customerDiscount.set(customerDiscount != null ? customerDiscount : BigDecimal.ZERO);
	}

	public ObjectProperty<BigDecimal> customerDiscountProperty() {
		return customerDiscount;
	}

	public Long getPromoItemId() {
		return promoItemId.get();
	}

	public void setPromoItemId(Long promoItemId) {
		this.promoItemId.set(promoItemId != null ? promoItemId : 0L);
	}

	public LongProperty promoItemIdProperty() {
		return promoItemId;
	}

	public String getPromoItemCode() {
		return promoItemCode.get();
	}

	public void setPromoItemCode(String promoItemCode) {
		this.promoItemCode.set(promoItemCode);
	}

	public StringProperty promoItemCodeProperty() {
		return promoItemCode;
	}

	public String getPromoItemName() {
		return promoItemName.get();
	}

	public void setPromoItemName(String promoItemName) {
		this.promoItemName.set(promoItemName);
	}

	public StringProperty promoItemNameProperty() {
		return promoItemName;
	}

	public String getPromoItemDescr() {
		return promoItemDescr.get();
	}

	public void setPromoItemDescr(String promoItemDescr) {
		this.promoItemDescr.set(promoItemDescr);
	}

	public StringProperty promoItemDescrProperty() {
		return promoItemDescr;
	}

	public Long getPromoPaymId() {
		return promoPaymId.get();
	}

	public void setPromoPaymId(Long promoPaymId) {
		this.promoPaymId.set(promoPaymId != null ? promoPaymId : 0L);
	}

	public LongProperty promoPaymIdProperty() {
		return promoPaymId;
	}

	public String getPromoPaymCode() {
		return promoPaymCode.get();
	}

	public void setPromoPaymCode(String promoPaymCode) {
		this.promoPaymCode.set(promoPaymCode);
	}

	public StringProperty promoPaymCodeProperty() {
		return promoPaymCode;
	}

	public String getPromoPaymName() {
		return promoPaymName.get();
	}

	public void setPromoPaymName(String promoPaymName) {
		this.promoPaymName.set(promoPaymName);
	}

	public StringProperty promoPaymNameProperty() {
		return promoPaymName;
	}

	public String getPromoPaymDescr() {
		return promoPaymDescr.get();
	}

	public void setPromoPaymDescr(String promoPaymDescr) {
		this.promoPaymDescr.set(promoPaymDescr);
	}

	public StringProperty promoPaymDescrProperty() {
		return promoPaymDescr;
	}

	public Long getPromoNextTxId() {
		return promoNextTxId.get();
	}

	public void setPromoNextTxId(Long promoNextTxId) {
		this.promoNextTxId.set(promoNextTxId != null ? promoNextTxId : 0L);
	}

	public LongProperty promoNextTxIdProperty() {
		return promoNextTxId;
	}

	public String getPromoNextTxCode() {
		return promoNextTxCode.get();
	}

	public void setPromoNextTxCode(String promoNextTxCode) {
		this.promoNextTxCode.set(promoNextTxCode);
	}

	public StringProperty promoNextTxCodeProperty() {
		return promoNextTxCode;
	}

	public String getPromoNextTxName() {
		return promoNextTxName.get();
	}

	public void setPromoNextTxName(String promoNextTxName) {
		this.promoNextTxName.set(promoNextTxName);
	}

	public StringProperty promoNextTxNameProperty() {
		return promoNextTxName;
	}

	public String getPromoNextTxDescr() {
		return promoNextTxDescr.get();
	}

	public void setPromoNextTxDescr(String promoNextTxDescr) {
		this.promoNextTxDescr.set(promoNextTxDescr);
	}

	public StringProperty promoNextTxDescrProperty() {
		return promoNextTxDescr;
	}

	public int getPromoItemCount() {
		return promoItemCount.get();
	}

	public void setPromoItemCount(int promoItemCount) {
		this.promoItemCount.set(promoItemCount);
	}

	public IntegerProperty promoItemCountProperty() {
		return promoItemCount;
	}

	public int getPromoPaymCount() {
		return promoPaymCount.get();
	}

	public void setPromoPaymCount(int promoPaymCount) {
		this.promoPaymCount.set(promoPaymCount);
	}

	public IntegerProperty promoPaymCountProperty() {
		return promoPaymCount;
	}

	public int getPromoNextTxCount() {
		return promoNextTxCount.get();
	}

	public void setPromoNextTxCount(int promoNextTxCount) {
		this.promoNextTxCount.set(promoNextTxCount);
	}

	public IntegerProperty promoNextTxCountProperty() {
		return promoNextTxCount;
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
