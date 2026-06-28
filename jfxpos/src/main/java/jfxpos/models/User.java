package jfxpos.models;

import jfxpos.Model;
import java.time.LocalDateTime;

public class User extends Model {
	private int id;
	private String username;
	private String password;
	private String role;
	private boolean isActive;
	private LocalDateTime createdAt;

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
}
