package jfxpos.repository;

import jfxpos.models.Salesperson;
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

public class SalespersonRepository {
	private static final Logger logger = PosLogger.createLogger(SalespersonRepository.class.getName());

	public List<Salesperson> search(String keyword) {
		List<Salesperson> list = new ArrayList<>();
		boolean isEmpty = (keyword == null || keyword.trim().isEmpty());
		String sql;
		if (isEmpty) {
			sql = String.format("SELECT FIRST 30 * FROM %s WHERE %s = ? ORDER BY %s DESC",
					Salesperson.Contract.TABLE_NAME,
					Salesperson.Contract.Columns.ISDISABLED,
					Salesperson.Contract.Columns.DATATIMESTAMP);
		} else {
			sql = String.format("SELECT * FROM %s WHERE (UPPER(%s) = ? OR UPPER(%s) LIKE ?) AND %s = ? ORDER BY %s",
					Salesperson.Contract.TABLE_NAME,
					Salesperson.Contract.Columns.NIK,
					Salesperson.Contract.Columns.NAME,
					Salesperson.Contract.Columns.ISDISABLED,
					Salesperson.Contract.Columns.NAME);
		}

		try (Connection conn = DbPool.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			if (isEmpty) {
				ps.setBoolean(1, false);
			} else {
				String kw = keyword == null ? "" : keyword.trim().toUpperCase();
				ps.setString(1, kw);
				ps.setString(2, "%" + kw + "%");
				ps.setBoolean(3, false);
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Salesperson sp = new Salesperson();
					sp.setSalespersonId(rs.getInt(Salesperson.Contract.Columns.ID));
					sp.setSalespersonNik(rs.getString(Salesperson.Contract.Columns.NIK));
					sp.setSalespersonName(rs.getString(Salesperson.Contract.Columns.NAME));
					sp.setSalespersonIsDisabled(rs.getBoolean(Salesperson.Contract.Columns.ISDISABLED));
					
					int brandId = rs.getInt(Salesperson.Contract.Columns.BRAND_ID);
					if (!rs.wasNull()) {
						sp.setBrandId(brandId);
					}
					
					int siteId = rs.getInt(Salesperson.Contract.Columns.SITE_ID);
					if (!rs.wasNull()) {
						sp.setSiteId(siteId);
					}
					
					list.add(sp);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error searching salesperson by keyword: " + keyword, e);
		}
		return list;
	}
}
