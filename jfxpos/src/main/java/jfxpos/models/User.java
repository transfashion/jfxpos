package jfxpos.models;

import jfxpos.Model;
import java.time.LocalDateTime;

public class User extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "USERS";

		public static final class Columns {
			public static final String ID = "USER_ID";
			public static final String USERNAME = "USERNAME";
			public static final String PASSWORD = "PASSWORD";
			public static final String ROLE = "ROLE";
			public static final String IS_ACTIVE = "IS_ACTIVE";
			public static final String CREATED_AT = "CREATED_AT";
			public static final String MODIFIED_AT = "MODIFIED_AT";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
		}
	}

	private int id;
	private String username;
	private String password;
	private String role;
	private boolean isActive;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private LocalDateTime dataTimestamp;

	public User() {
	}

	public User(int id, String username, String password, String role, boolean isActive, LocalDateTime createdAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}

	public User(int id, String username, String password, String role, boolean isActive, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime dataTimestamp) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
