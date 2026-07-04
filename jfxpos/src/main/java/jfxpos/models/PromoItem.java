package jfxpos.models;

import jfxpos.Model;

public class PromoItem extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "PROMO_ITEM";

		public static final class Columns {
			public static final String ID = "PROMOITEM_ID";
			public static final String NOTE = "PROMOITEM_NOTE";
		}
	}

	private int id;
	private String note;

	public PromoItem() {
	}

	public PromoItem(int id, String note) {
		this.id = id;
		this.note = note;
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
}
