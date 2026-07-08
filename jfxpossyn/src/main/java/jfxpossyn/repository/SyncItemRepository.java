package jfxpossyn.repository;

import jfxpossyn.model.SyncItem;
import jfxpossyn.util.DbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncItemRepository {
	private static final Logger logger = Logger.getLogger(SyncItemRepository.class.getName());

	/**
	 * Retrieves the latest SyncItem based on SYNCITEM_DATETIME.
	 *
	 * @return The latest SyncItem, or null if no sync has occurred yet.
	 * @throws SQLException If database access fails.
	 */
	public SyncItem getLatestSyncItem() throws SQLException {
		String sql = String.format("SELECT FIRST 1 * FROM %s WHERE %s = ? ORDER BY %s DESC, %s DESC",
				SyncItem.Contract.TABLE_NAME,
				SyncItem.Contract.Columns.ISERROR,
				SyncItem.Contract.Columns.DATETIME,
				SyncItem.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setBoolean(1, false);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int id = rs.getInt(SyncItem.Contract.Columns.ID);
					Timestamp tsDatetime = rs.getTimestamp(SyncItem.Contract.Columns.DATETIME);
					LocalDateTime datetime = tsDatetime != null ? tsDatetime.toLocalDateTime() : null;

					java.sql.Date dtClearon = rs.getDate(SyncItem.Contract.Columns.CLEARON);
					LocalDate clearon = dtClearon != null ? dtClearon.toLocalDate() : null;

					boolean isCompleted = rs.getBoolean(SyncItem.Contract.Columns.ISCOMPLETED);

					Timestamp tsCompleted = rs.getTimestamp(SyncItem.Contract.Columns.COMPLETEDDATE);
					LocalDateTime completedDate = tsCompleted != null ? tsCompleted.toLocalDateTime() : null;

					int durationVal = rs.getInt(SyncItem.Contract.Columns.DURATION);
					Integer duration = rs.wasNull() ? null : durationVal;

					boolean isError = rs.getBoolean(SyncItem.Contract.Columns.ISERROR);
					String errorMessage = rs.getString(SyncItem.Contract.Columns.ERRORMESSAGE);

					return new SyncItem(id, datetime, clearon, isCompleted, completedDate, duration, isError, errorMessage);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding latest SyncItem", e);
			throw e;
		}
		return null;
	}

	/**
	 * Inserts a new SyncItem and returns its generated ID.
	 *
	 * @param item The SyncItem details to insert.
	 * @return The generated ID, or -1 if insertion failed.
	 * @throws SQLException If database access fails.
	 */
	public int insertSyncItem(SyncItem item) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
				SyncItem.Contract.TABLE_NAME,
				SyncItem.Contract.Columns.DATETIME,
				SyncItem.Contract.Columns.CLEARON,
				SyncItem.Contract.Columns.ISCOMPLETED,
				SyncItem.Contract.Columns.ISERROR,
				SyncItem.Contract.Columns.ERRORMESSAGE);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setTimestamp(1, Timestamp.valueOf(item.getDatetime()));
			ps.setDate(2, item.getClearon() != null ? java.sql.Date.valueOf(item.getClearon()) : null);
			ps.setBoolean(3, item.isCompleted());
			ps.setBoolean(4, item.isError());
			ps.setString(5, item.getErrorMessage());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error inserting SyncItem", e);
			throw e;
		}
		return -1;
	}

	/**
	 * Updates the completion status, completed date, duration, error status, and error message of a SyncItem.
	 *
	 * @param item The SyncItem with updated fields.
	 * @throws SQLException If database access fails.
	 */
	public void updateSyncItem(SyncItem item) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
				SyncItem.Contract.TABLE_NAME,
				SyncItem.Contract.Columns.ISCOMPLETED,
				SyncItem.Contract.Columns.COMPLETEDDATE,
				SyncItem.Contract.Columns.DURATION,
				SyncItem.Contract.Columns.ISERROR,
				SyncItem.Contract.Columns.ERRORMESSAGE,
				SyncItem.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, item.isCompleted());
			if (item.getCompletedDate() != null) {
				ps.setTimestamp(2, Timestamp.valueOf(item.getCompletedDate()));
			} else {
				ps.setNull(2, java.sql.Types.TIMESTAMP);
			}

			if (item.getDuration() != null) {
				ps.setInt(3, item.getDuration());
			} else {
				ps.setNull(3, java.sql.Types.INTEGER);
			}

			ps.setBoolean(4, item.isError());
			if (item.getErrorMessage() != null) {
				ps.setString(5, item.getErrorMessage());
			} else {
				ps.setNull(5, java.sql.Types.CLOB);
			}

			ps.setInt(6, item.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating SyncItem with ID: " + item.getId(), e);
			throw e;
		}
	}
}
