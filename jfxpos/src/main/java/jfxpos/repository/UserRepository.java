package jfxpos.repository;

import jfxpos.models.User;
import jfxpos.models.UserContract;
import jfxpos.util.DbPool;
import jfxpos.util.PosLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository {
	private static final Logger logger = PosLogger.createLogger(UserRepository.class.getName());

	public User findByUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			return null;
		}

		String sql = String.format("SELECT * FROM %s WHERE %s = ?",
				UserContract.TABLE_NAME, UserContract.Columns.USERNAME);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username.trim());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int id = rs.getInt(UserContract.Columns.ID);
					String user = rs.getString(UserContract.Columns.USERNAME);
					String password = rs.getString(UserContract.Columns.PASSWORD);
					String role = rs.getString(UserContract.Columns.ROLE);
					boolean isActive = rs.getBoolean(UserContract.Columns.IS_ACTIVE);

					Timestamp ts = rs.getTimestamp(UserContract.Columns.CREATED_AT);
					LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;

					return new User(id, user, password, role, isActive, createdAt);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding user by username: " + username, e);
		}
		return null;
	}
}
