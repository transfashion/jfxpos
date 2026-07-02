package jfxpos.models;

import jfxpos.Model;
import java.time.LocalDateTime;

public class Channel extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "CHANNEL";

		public static final class Columns {
			public static final String ID = "CHANNEL_ID";
			public static final String CHANNEL_NAME = "CHANNEL_NAME";
			public static final String IS_ACTIVE = "IS_ACTIVE";
			public static final String CREATED_AT = "CREATED_AT";
			public static final String MODIFIED_AT = "MODIFIED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private int id;
	private String channelName;
	private boolean isActive;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private LocalDateTime dataTimestamp;

	public Channel() {
	}

	public Channel(int id, String channelName, boolean isActive, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime dataTimestamp) {
		this.id = id;
		this.channelName = channelName;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.dataTimestamp = dataTimestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
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
