package jfxpos.repository;

import jfxpos.models.Channel;
import jfxpos.util.DbPool;
import jfxpos.util.PosLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelRepository {
	private static final Logger logger = PosLogger.createLogger(ChannelRepository.class.getName());

	public Channel findById(int id) {
		String sql = String.format("SELECT * FROM %s WHERE %s = ?",
				Channel.Contract.TABLE_NAME, Channel.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToChannel(rs);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding channel by ID: " + id, e);
		}
		return null;
	}

	public List<Channel> findAll() {
		List<Channel> channels = new ArrayList<>();
		String sql = String.format("SELECT * FROM %s", Channel.Contract.TABLE_NAME);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				channels.add(mapResultSetToChannel(rs));
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding all channels", e);
		}
		return channels;
	}

	public boolean save(Channel channel) {
		if (channel == null) {
			return false;
		}

		boolean exists = findById(channel.getId()) != null;
		if (exists) {
			String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
					Channel.Contract.TABLE_NAME,
					Channel.Contract.Columns.CHANNEL_NAME,
					Channel.Contract.Columns.IS_ACTIVE,
					Channel.Contract.Columns.MODIFIED_AT,
					Channel.Contract.Columns.DATATIMESTAMP,
					Channel.Contract.Columns.ID);

			try (Connection conn = DbPool.getConnection();
					PreparedStatement ps = conn.prepareStatement(sql)) {

				ps.setString(1, channel.getChannelName());
				ps.setBoolean(2, channel.isActive());
				ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
				ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
				ps.setInt(5, channel.getId());

				return ps.executeUpdate() > 0;
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Error updating channel: " + channel.getId(), e);
			}
		} else {
			String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
					Channel.Contract.TABLE_NAME,
					Channel.Contract.Columns.ID,
					Channel.Contract.Columns.CHANNEL_NAME,
					Channel.Contract.Columns.IS_ACTIVE,
					Channel.Contract.Columns.CREATED_AT,
					Channel.Contract.Columns.MODIFIED_AT,
					Channel.Contract.Columns.DATATIMESTAMP);

			try (Connection conn = DbPool.getConnection();
					PreparedStatement ps = conn.prepareStatement(sql)) {

				ps.setInt(1, channel.getId());
				ps.setString(2, channel.getChannelName());
				ps.setBoolean(3, channel.isActive());
				
				LocalDateTime now = LocalDateTime.now();
				ps.setTimestamp(4, Timestamp.valueOf(now));
				ps.setNull(5, java.sql.Types.TIMESTAMP);
				ps.setTimestamp(6, Timestamp.valueOf(now));

				return ps.executeUpdate() > 0;
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Error inserting channel: " + channel.getId(), e);
			}
		}
		return false;
	}

	private Channel mapResultSetToChannel(ResultSet rs) throws SQLException {
		int id = rs.getInt(Channel.Contract.Columns.ID);
		String name = rs.getString(Channel.Contract.Columns.CHANNEL_NAME);
		boolean isActive = rs.getBoolean(Channel.Contract.Columns.IS_ACTIVE);

		Timestamp tsCreated = rs.getTimestamp(Channel.Contract.Columns.CREATED_AT);
		LocalDateTime createdAt = tsCreated != null ? tsCreated.toLocalDateTime() : null;

		Timestamp tsModified = rs.getTimestamp(Channel.Contract.Columns.MODIFIED_AT);
		LocalDateTime modifiedAt = tsModified != null ? tsModified.toLocalDateTime() : null;

		Timestamp tsData = rs.getTimestamp(Channel.Contract.Columns.DATATIMESTAMP);
		LocalDateTime dataTimestamp = tsData != null ? tsData.toLocalDateTime() : null;

		return new Channel(id, name, isActive, createdAt, modifiedAt, dataTimestamp);
	}
}
