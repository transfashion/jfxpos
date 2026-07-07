package jfxpossyn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SyncItem extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "SYNCITEM";

		public static final class Columns {
			public static final String ID = "SYNCITEM_ID";
			public static final String DATETIME = "SYNCITEM_DATETIME";
			public static final String CLEARON = "SYNCITEM_CLEARON";
			public static final String ISCOMPLETED = "SYNCITEM_ISCOMPLETED";
			public static final String COMPLETEDDATE = "SYNCITEM_COMPLETEDDATE";
			public static final String DURATION = "SYNCITEM_DURATION";
		}
	}

	private int id;
	private LocalDateTime datetime;
	private LocalDate clearon;
	private boolean isCompleted;
	private LocalDateTime completedDate;
	private Integer duration;

	public SyncItem() {
	}

	public SyncItem(int id, LocalDateTime datetime, LocalDate clearon, boolean isCompleted, LocalDateTime completedDate, Integer duration) {
		this.id = id;
		this.datetime = datetime;
		this.clearon = clearon;
		this.isCompleted = isCompleted;
		this.completedDate = completedDate;
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public LocalDate getClearon() {
		return clearon;
	}

	public void setClearon(LocalDate clearon) {
		this.clearon = clearon;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public LocalDateTime getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(LocalDateTime completedDate) {
		this.completedDate = completedDate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
