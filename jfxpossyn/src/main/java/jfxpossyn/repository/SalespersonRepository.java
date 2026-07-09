package jfxpossyn.repository;

import jfxpossyn.model.Salesperson;
import jfxpossyn.util.DbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalespersonRepository {
	private static final Logger logger = Logger.getLogger(SalespersonRepository.class.getName());

	/**
	 * Retrieves the latest DATATIMESTAMP from the SALESPERSON table, formatted as an ISO 8601 UTC string.
	 * Returns "0" if the table is empty or does not have any records.
	 *
	 * @return ISO 8601 UTC string of the latest timestamp, or "0".
	 * @throws SQLException If database access fails.
	 */
	public String getLastDataTimestamp() throws SQLException {
		String sql = String.format("SELECT MAX(%s) FROM %s",
				Salesperson.Contract.Columns.DATATIMESTAMP,
				Salesperson.Contract.TABLE_NAME);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				Timestamp ts = rs.getTimestamp(1);
				if (ts != null) {
					return DateTimeFormatter.ISO_INSTANT.format(ts.toInstant());
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error retrieving last datatimestamp from SALESPERSON table", e);
			throw e;
		}
		return "0";
	}

	/**
	 * Batch upserts salespersons into the SALESPERSON table.
	 *
	 * @param salespersons List of Salesperson models to upsert.
	 * @throws SQLException If database access fails.
	 */
	public void saveSalespersons(List<Salesperson> salespersons) throws SQLException {
		if (salespersons == null || salespersons.isEmpty()) {
			return;
		}

		String sql = String.format(
				"UPDATE OR INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?) MATCHING (%s)",
				Salesperson.Contract.TABLE_NAME,
				Salesperson.Contract.Columns.SALESPERSON_ID,
				Salesperson.Contract.Columns.SALESPERSON_NIK,
				Salesperson.Contract.Columns.SALESPERSON_NAME,
				Salesperson.Contract.Columns.SALESPERSON_ISDISABLED,
				Salesperson.Contract.Columns.BRAND_ID,
				Salesperson.Contract.Columns.SITE_ID,
				Salesperson.Contract.Columns.DATATIMESTAMP,
				Salesperson.Contract.Columns.MD5HASH,
				Salesperson.Contract.Columns.SALESPERSON_ID
		);

		try (Connection conn = DbPool.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				for (Salesperson sp : salespersons) {
					ps.setInt(1, sp.getSalespersonId());
					ps.setString(2, sp.getSalespersonNik());
					ps.setString(3, sp.getSalespersonName());
					ps.setBoolean(4, sp.isSalespersonIsDisabled());

					if (sp.getBrandId() != null) {
						ps.setInt(5, sp.getBrandId());
					} else {
						ps.setNull(5, java.sql.Types.INTEGER);
					}

					if (sp.getSiteId() != null) {
						ps.setInt(6, sp.getSiteId());
					} else {
						ps.setNull(6, java.sql.Types.INTEGER);
					}

					if (sp.getDataTimestamp() != null) {
						ps.setTimestamp(7, Timestamp.valueOf(sp.getDataTimestamp()));
					} else {
						ps.setNull(7, java.sql.Types.TIMESTAMP);
					}

					ps.setString(8, sp.getMd5Hash());

					ps.addBatch();
				}
				ps.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				logger.log(Level.SEVERE, "Error executing batch upsert for SALESPERSON", e);
				throw e;
			}
		}
	}
}
