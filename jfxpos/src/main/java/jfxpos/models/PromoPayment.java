package jfxpos.models;

import jfxpos.Model;
import java.math.BigDecimal;

public class PromoPayment extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "PROMO_PAYMENT";

		public static final class Columns {
			public static final String ID = "PROMOPAYM_ID";
			public static final String NOTE = "PROMOPAYM_NOTE";
			public static final String VALUE = "PROMOPAYM_VALUE";
		}
	}

	private int id;
	private String note;
	private BigDecimal value;

	public PromoPayment() {
		this.value = BigDecimal.ZERO;
	}

	public PromoPayment(int id, String note, BigDecimal value) {
		this.id = id;
		this.note = note;
		this.value = value != null ? value : BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value != null ? value : BigDecimal.ZERO;
	}
}
