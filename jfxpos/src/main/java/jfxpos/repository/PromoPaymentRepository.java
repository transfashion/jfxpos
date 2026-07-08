package jfxpos.repository;

import jfxpos.models.PromoPayment;
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

public class PromoPaymentRepository {
	private static final Logger logger = PosLogger.createLogger(PromoPaymentRepository.class.getName());

	public List<PromoPayment> findAllActive() {
		List<PromoPayment> promos = new ArrayList<>();
		promos.add(new PromoPayment(0, "NONE"));
		String sql = String.format("SELECT * FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= CURRENT_TIME) AND (%s IS NULL OR %s >= CURRENT_TIME) " +
				"ORDER BY %s ASC, %s ASC, %s ASC, %s ASC",
				PromoPayment.Contract.TABLE_NAME, 
				PromoPayment.Contract.Columns.ISACTIVE,
				PromoPayment.Contract.Columns.DATESTART, PromoPayment.Contract.Columns.DATESTART,
				PromoPayment.Contract.Columns.DATEEND, PromoPayment.Contract.Columns.DATEEND,
				PromoPayment.Contract.Columns.TIMESTART, PromoPayment.Contract.Columns.TIMESTART,
				PromoPayment.Contract.Columns.TIMEEND, PromoPayment.Contract.Columns.TIMEEND,
				PromoPayment.Contract.Columns.DATESTART, PromoPayment.Contract.Columns.DATEEND,
				PromoPayment.Contract.Columns.TIMESTART, PromoPayment.Contract.Columns.TIMEEND);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setBoolean(1, true);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					promos.add(mapResultSetToPromoPayment(rs));
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding all active promo payments", e);
		}
		return promos;
	}

	public PromoPayment findById(int id) {
		String sql = String.format("SELECT * FROM %s WHERE %s = ?",
				PromoPayment.Contract.TABLE_NAME, PromoPayment.Contract.Columns.ID);

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToPromoPayment(rs);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error finding promo payment by ID: " + id, e);
		}
		return null;
	}

	public int getActivePromoCount() throws SQLException {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ? " +
				"AND (%s IS NULL OR %s <= CURRENT_DATE) AND (%s IS NULL OR %s >= CURRENT_DATE) " +
				"AND (%s IS NULL OR %s <= CURRENT_TIME) AND (%s IS NULL OR %s >= CURRENT_TIME)",
				PromoPayment.Contract.TABLE_NAME, 
				PromoPayment.Contract.Columns.ISACTIVE,
				PromoPayment.Contract.Columns.DATESTART, PromoPayment.Contract.Columns.DATESTART,
				PromoPayment.Contract.Columns.DATEEND, PromoPayment.Contract.Columns.DATEEND,
				PromoPayment.Contract.Columns.TIMESTART, PromoPayment.Contract.Columns.TIMESTART,
				PromoPayment.Contract.Columns.TIMEEND, PromoPayment.Contract.Columns.TIMEEND);

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

	public PromoPayment getDefaultPromo() {
		List<PromoPayment> active = findAllActive();
		if (active != null && active.size() > 1) {
			return active.get(1);
		}
		return null;
	}

	private PromoPayment mapResultSetToPromoPayment(ResultSet rs) throws SQLException {
		PromoPayment promo = new PromoPayment();
		promo.setId(rs.getInt(PromoPayment.Contract.Columns.ID));
		promo.setName(rs.getString(PromoPayment.Contract.Columns.NAME));
		promo.setCode(rs.getString(PromoPayment.Contract.Columns.CODE));
		promo.setDescr(rs.getString(PromoPayment.Contract.Columns.DESCR));
		promo.setIsactive(rs.getBoolean(PromoPayment.Contract.Columns.ISACTIVE));
		promo.setDatestart(rs.getDate(PromoPayment.Contract.Columns.DATESTART));
		promo.setDateend(rs.getDate(PromoPayment.Contract.Columns.DATEEND));
		promo.setTimestart(rs.getTime(PromoPayment.Contract.Columns.TIMESTART));
		promo.setTimeend(rs.getTime(PromoPayment.Contract.Columns.TIMEEND));
		promo.setData(rs.getString(PromoPayment.Contract.Columns.DATA));
		return promo;
	}
}
