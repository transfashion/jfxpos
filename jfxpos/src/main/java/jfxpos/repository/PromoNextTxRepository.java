package jfxpos.repository;

import jfxpos.models.PromoNextTx;
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

public class PromoNextTxRepository {
	private static final Logger logger = PosLogger.createLogger(PromoNextTxRepository.class.getName());

	public List<PromoNextTx> findAllActive() {
		List<PromoNextTx> promos = new ArrayList<>();
		promos.add(new PromoNextTx(0, "NONE"));
		String sql = String.format("SELECT * FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= CURRENT_TIME) AND (%s IS NULL OR %s >= CURRENT_TIME) " +
				"ORDER BY %s ASC, %s ASC, %s ASC, %s ASC",
				PromoNextTx.Contract.TABLE_NAME, 
				PromoNextTx.Contract.Columns.ISACTIVE,
				PromoNextTx.Contract.Columns.DATESTART, PromoNextTx.Contract.Columns.DATESTART,
				PromoNextTx.Contract.Columns.DATEEND, PromoNextTx.Contract.Columns.DATEEND,
				PromoNextTx.Contract.Columns.TIMESTART, PromoNextTx.Contract.Columns.TIMESTART,
				PromoNextTx.Contract.Columns.TIMEEND, PromoNextTx.Contract.Columns.TIMEEND,
				PromoNextTx.Contract.Columns.DATESTART, PromoNextTx.Contract.Columns.DATEEND,
				PromoNextTx.Contract.Columns.TIMESTART, PromoNextTx.Contract.Columns.TIMEEND);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, true);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					promos.add(mapResultSetToPromoNextTx(rs));
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding all active promo next transactions", e);
		}
		return promos;
	}

	public PromoNextTx findById(int id) {
		String sql = String.format("SELECT * FROM %s WHERE %s = ?",
				PromoNextTx.Contract.TABLE_NAME, PromoNextTx.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToPromoNextTx(rs);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding promo next tx by ID: " + id, e);
		}
		return null;
	}

	public int getActivePromoCount() throws SQLException {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= CURRENT_TIME) AND (%s IS NULL OR %s >= CURRENT_TIME)",
				PromoNextTx.Contract.TABLE_NAME, 
				PromoNextTx.Contract.Columns.ISACTIVE,
				PromoNextTx.Contract.Columns.DATESTART, PromoNextTx.Contract.Columns.DATESTART,
				PromoNextTx.Contract.Columns.DATEEND, PromoNextTx.Contract.Columns.DATEEND,
				PromoNextTx.Contract.Columns.TIMESTART, PromoNextTx.Contract.Columns.TIMESTART,
				PromoNextTx.Contract.Columns.TIMEEND, PromoNextTx.Contract.Columns.TIMEEND);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, true);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	public PromoNextTx getDefaultPromo() {
		List<PromoNextTx> active = findAllActive();
		if (active != null && active.size() > 1) {
			return active.get(1);
		}
		return null;
	}

	private PromoNextTx mapResultSetToPromoNextTx(ResultSet rs) throws SQLException {
		PromoNextTx promo = new PromoNextTx();
		promo.setId(rs.getInt(PromoNextTx.Contract.Columns.ID));
		promo.setName(rs.getString(PromoNextTx.Contract.Columns.NAME));
		promo.setCode(rs.getString(PromoNextTx.Contract.Columns.CODE));
		promo.setDescr(rs.getString(PromoNextTx.Contract.Columns.DESCR));
		promo.setIsactive(rs.getBoolean(PromoNextTx.Contract.Columns.ISACTIVE));
		promo.setDatestart(rs.getDate(PromoNextTx.Contract.Columns.DATESTART));
		promo.setDateend(rs.getDate(PromoNextTx.Contract.Columns.DATEEND));
		promo.setTimestart(rs.getTime(PromoNextTx.Contract.Columns.TIMESTART));
		promo.setTimeend(rs.getTime(PromoNextTx.Contract.Columns.TIMEEND));
		promo.setData(rs.getString(PromoNextTx.Contract.Columns.DATA));
		return promo;
	}
}
