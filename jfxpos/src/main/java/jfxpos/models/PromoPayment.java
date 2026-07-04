package jfxpos.models;

import java.sql.Date;
import java.sql.Time;
import jfxpos.Model;

public class PromoPayment extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "PROMOPAYM";

		public static final class Columns {
			public static final String ID = "PROMOPAYM_ID";
			public static final String NAME = "PROMOPAYM_NAME";
			public static final String CODE = "PROMOPAYM_CODE";
			public static final String DESCR = "PROMOPAYM_DESCR";
			public static final String ISACTIVE = "PROMOPAYM_ISACTIVE";
			public static final String DATESTART = "PROMOPAYM_DATESTART";
			public static final String DATEEND = "PROMOPAYM_DATEEND";
			public static final String DATA = "PROMOPAYM_DATA";
			public static final String TIMESTART = "PROMOPAYM_TIMESTART";
			public static final String TIMEEND = "PROMOPAYM_TIMEEND";
		}
	}

	private int id;
	private String name;
	private String code;
	private String descr;
	private Boolean isactive;
	private Date datestart;
	private Date dateend;
	private String data;
	private java.math.BigDecimal value;
	private Time timestart;
	private Time timeend;

	public PromoPayment() {
	}

	public PromoPayment(int id, String name) {
		this.id = id;
		this.name = name;
		this.code = "P" + id;
		this.value = java.math.BigDecimal.ZERO;
	}

	public PromoPayment(int id, String name, java.math.BigDecimal value) {
		this.id = id;
		this.name = name;
		this.code = "P" + id;
		this.value = value != null ? value : java.math.BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public Boolean isActive() {
		return isactive != null && isactive;
	}

	public Date getDatestart() {
		return datestart;
	}

	public void setDatestart(Date datestart) {
		this.datestart = datestart;
	}

	public Date getDateend() {
		return dateend;
	}

	public void setDateend(Date dateend) {
		this.dateend = dateend;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNote() {
		return name;
	}

	public void setNote(String note) {
		this.name = note;
	}

	public java.math.BigDecimal getValue() {
		return value;
	}

	public void setValue(java.math.BigDecimal value) {
		this.value = value != null ? value : java.math.BigDecimal.ZERO;
	}

	public Time getTimestart() {
		return timestart;
	}

	public void setTimestart(Time timestart) {
		this.timestart = timestart;
	}

	public Time getTimeend() {
		return timeend;
	}

	public void setTimeend(Time timeend) {
		this.timeend = timeend;
	}
}
