package jfxpos.models;

import jfxpos.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrxPaym extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "TRX_PAYM";

		public static final class Columns {
			public static final String ID = "TRX_PAYM_ID";
			public static final String TRX_ID = "TRX_ID";
			public static final String PAYMDEV_ID = "PAYMDEV_ID";
			public static final String PAYMETHOD_ID = "PAYMETHOD_ID";
			public static final String PAY_AMOUNT = "PAY_AMOUNT";
			public static final String PAY_VALUE = "PAY_VALUE";
			public static final String PAY_RETURN = "PAY_RETURN";
			public static final String CARD_NO = "CARD_NO";
			public static final String CARD_NAME = "CARD_NAME";
			public static final String APPROVAL_CODE = "APPROVAL_CODE";
			public static final String IS_ADVANCE = "IS_ADVANCE";
			public static final String CUSTDEPO_ID = "CUSTDEPO_ID";
			public static final String CUSTDEPO_DOC = "CUSTDEPO_DOC";
			public static final String CUSTDEPO_DESC = "CUSTDEPO_DESC";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private long id;
	private long trxId;
	private Integer paymdevId;
	private int paymethodId;
	private BigDecimal payAmount = BigDecimal.ZERO;
	private BigDecimal payValue = BigDecimal.ZERO;
	private BigDecimal payReturn = BigDecimal.ZERO;
	private String cardNo;
	private String cardName;
	private String approvalCode;
	private boolean isAdvance = true;
	private Long custdepoId;
	private String custdepoDoc;
	private String custdepoDesc;
	private LocalDateTime dataTimestamp;

	public TrxPaym() {
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

	public Integer getPaymdevId() {
		return paymdevId;
	}

	public void setPaymdevId(Integer paymdevId) {
		this.paymdevId = paymdevId;
	}

	public int getPaymethodId() {
		return paymethodId;
	}

	public void setPaymethodId(int paymethodId) {
		this.paymethodId = paymethodId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getPayValue() {
		return payValue;
	}

	public void setPayValue(BigDecimal payValue) {
		this.payValue = payValue;
	}

	public BigDecimal getPayReturn() {
		return payReturn;
	}

	public void setPayReturn(BigDecimal payReturn) {
		this.payReturn = payReturn;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public boolean isAdvance() {
		return isAdvance;
	}

	public void setAdvance(boolean advance) {
		isAdvance = advance;
	}

	public Long getCustdepoId() {
		return custdepoId;
	}

	public void setCustdepoId(Long custdepoId) {
		this.custdepoId = custdepoId;
	}

	public String getCustdepoDoc() {
		return custdepoDoc;
	}

	public void setCustdepoDoc(String custdepoDoc) {
		this.custdepoDoc = custdepoDoc;
	}

	public String getCustdepoDesc() {
		return custdepoDesc;
	}

	public void setCustdepoDesc(String custdepoDesc) {
		this.custdepoDesc = custdepoDesc;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}
}
