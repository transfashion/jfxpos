package jfxpossyn.repository;

import jfxpossyn.model.Item;
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

public class ItemRepository {
	private static final Logger logger = Logger.getLogger(ItemRepository.class.getName());

	/**
	 * Retrieves the latest DATATIMESTAMP from the ITEM table, formatted as an ISO 8601 UTC string.
	 * Returns "0" if the table is empty or does not have any records.
	 *
	 * @return ISO 8601 UTC string of the latest timestamp, or "0".
	 * @throws SQLException If database access fails.
	 */
	public String getLastDataTimestamp() throws SQLException {
		String sql = String.format("SELECT MAX(%s) FROM %s",
				Item.Contract.Columns.DATATIMESTAMP,
				Item.Contract.TABLE_NAME);

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
			logger.log(Level.SEVERE, "Error retrieving last datatimestamp from ITEM table", e);
			throw e;
		}
		return "0";
	}

	/**
	 * Batch upserts (inserts or updates) items into the ITEM table using connection pools
	 * and commits them in a single transaction.
	 *
	 * @param items List of Item models to upsert.
	 * @throws SQLException If database access fails.
	 */
	public void saveItems(List<Item> items) throws SQLException {
		if (items == null || items.isEmpty()) {
			return;
		}

		String sql = String.format(
				"UPDATE OR INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) MATCHING (%s)",
				Item.Contract.TABLE_NAME,
				Item.Contract.Columns.ITEM_ID,
				Item.Contract.Columns.ITEM_ART,
				Item.Contract.Columns.ITEM_COL,
				Item.Contract.Columns.ITEM_SIZE,
				Item.Contract.Columns.ITEM_DESCR,
				Item.Contract.Columns.ITEM_PRICEGROSS,
				Item.Contract.Columns.ITEM_PRICE,
				Item.Contract.Columns.ITEM_DISC,
				Item.Contract.Columns.ITEM_ISSPECIALPRICE,
				Item.Contract.Columns.ITEM_ISDISABLED,
				Item.Contract.Columns.CTG_ID,
				Item.Contract.Columns.CTG_NAME,
				Item.Contract.Columns.UNIT_ID,
				Item.Contract.Columns.UNIT_NAME,
				Item.Contract.Columns.STRUCT_ID,
				Item.Contract.Columns.STRUCT_NAME,
				Item.Contract.Columns.BRAND_ID,
				Item.Contract.Columns.BRAND_NAME,
				Item.Contract.Columns.DATATIMESTAMP,
				Item.Contract.Columns.MD5HASH,
				Item.Contract.Columns.ITEM_ID
		);

		try (Connection conn = DbPool.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				for (Item item : items) {
					ps.setLong(1, item.getItemId());
					ps.setString(2, item.getItemArt());
					ps.setString(3, item.getItemCol());
					ps.setString(4, item.getItemSize());
					ps.setString(5, item.getItemDescr());
					ps.setBigDecimal(6, item.getItemPriceGross());
					ps.setBigDecimal(7, item.getItemPrice());
					ps.setBigDecimal(8, item.getItemDisc());
					ps.setBoolean(9, item.isItemIsSpecialPrice());
					ps.setBoolean(10, item.isItemIsDisabled());

					if (item.getCtgId() != null) {
						ps.setInt(11, item.getCtgId());
					} else {
						ps.setNull(11, java.sql.Types.INTEGER);
					}
					ps.setString(12, item.getCtgName());

					if (item.getUnitId() != null) {
						ps.setInt(13, item.getUnitId());
					} else {
						ps.setNull(13, java.sql.Types.INTEGER);
					}
					ps.setString(14, item.getUnitName());

					if (item.getStructId() != null) {
						ps.setInt(15, item.getStructId());
					} else {
						ps.setNull(15, java.sql.Types.INTEGER);
					}
					ps.setString(16, item.getStructName());

					if (item.getBrandId() != null) {
						ps.setInt(17, item.getBrandId());
					} else {
						ps.setNull(17, java.sql.Types.INTEGER);
					}
					ps.setString(18, item.getBrandName());

					if (item.getDataTimestamp() != null) {
						ps.setTimestamp(19, Timestamp.valueOf(item.getDataTimestamp()));
					} else {
						ps.setNull(19, java.sql.Types.TIMESTAMP);
					}
					ps.setString(20, item.getMd5Hash());

					ps.addBatch();
				}
				ps.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				logger.log(Level.SEVERE, "Failed batch insert/update of items", e);
				throw e;
			}
		}
	}
}
