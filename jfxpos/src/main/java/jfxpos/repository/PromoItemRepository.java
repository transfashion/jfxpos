package jfxpos.repository;

import jfxpos.models.PromoItem;
import jfxpos.util.DbPool;
import jfxpos.util.PosLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PromoItemRepository {
	private static final Logger logger = PosLogger.createLogger(PromoItemRepository.class.getName());

	public List<PromoItem> findAllActive() {
		List<PromoItem> promos = new ArrayList<>();
		promos.add(new PromoItem(0, "NONE"));
		String sql = String.format("SELECT * FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= LOCALTIME) AND (%s IS NULL OR %s >= LOCALTIME) " +
				"ORDER BY %s ASC, %s ASC, %s ASC, %s ASC",
				PromoItem.Contract.TABLE_NAME,
				PromoItem.Contract.Columns.ISACTIVE,
				PromoItem.Contract.Columns.DATESTART, PromoItem.Contract.Columns.DATESTART,
				PromoItem.Contract.Columns.DATEEND, PromoItem.Contract.Columns.DATEEND,
				PromoItem.Contract.Columns.TIMESTART, PromoItem.Contract.Columns.TIMESTART,
				PromoItem.Contract.Columns.TIMEEND, PromoItem.Contract.Columns.TIMEEND,
				PromoItem.Contract.Columns.DATESTART, PromoItem.Contract.Columns.DATEEND,
				PromoItem.Contract.Columns.TIMESTART, PromoItem.Contract.Columns.TIMEEND);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, true);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					promos.add(mapResultSetToPromoItem(rs));
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding all active promo items", e);
		}
		return promos;
	}

	public PromoItem findById(int id) {
		String sql = String.format("SELECT * FROM %s WHERE %s = ?",
				PromoItem.Contract.TABLE_NAME, PromoItem.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToPromoItem(rs);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding promo item by ID: " + id, e);
		}
		return null;
	}

	public int getActivePromoCount() {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= LOCALTIME) AND (%s IS NULL OR %s >= LOCALTIME)",
				PromoItem.Contract.TABLE_NAME,
				PromoItem.Contract.Columns.ISACTIVE,
				PromoItem.Contract.Columns.DATESTART, PromoItem.Contract.Columns.DATESTART,
				PromoItem.Contract.Columns.DATEEND, PromoItem.Contract.Columns.DATEEND,
				PromoItem.Contract.Columns.TIMESTART, PromoItem.Contract.Columns.TIMESTART,
				PromoItem.Contract.Columns.TIMEEND, PromoItem.Contract.Columns.TIMEEND);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, true);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error getting active promo items count", e);
		}
		return 0;
	}

	private PromoItem mapResultSetToPromoItem(ResultSet rs) throws SQLException {
		PromoItem promo = new PromoItem();
		promo.setId(rs.getInt(PromoItem.Contract.Columns.ID));
		promo.setName(rs.getString(PromoItem.Contract.Columns.NAME));
		promo.setCode(rs.getString(PromoItem.Contract.Columns.CODE));
		promo.setDescr(rs.getString(PromoItem.Contract.Columns.DESCR));
		promo.setIsactive(rs.getBoolean(PromoItem.Contract.Columns.ISACTIVE));
		promo.setDatestart(rs.getDate(PromoItem.Contract.Columns.DATESTART));
		promo.setDateend(rs.getDate(PromoItem.Contract.Columns.DATEEND));
		promo.setTimestart(rs.getTime(PromoItem.Contract.Columns.TIMESTART));
		promo.setTimeend(rs.getTime(PromoItem.Contract.Columns.TIMEEND));
		promo.setData(rs.getString(PromoItem.Contract.Columns.DATA));
		return promo;
	}

	public PromoItem getDefaultPromo() {
		List<PromoItem> active = findAllActive();
		if (active != null && active.size() > 1) {
			return active.get(1);
		}
		return null;
	}
}
