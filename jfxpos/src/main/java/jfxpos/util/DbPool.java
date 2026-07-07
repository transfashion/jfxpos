package jfxpos.util;

import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;

import java.sql.Connection;
import java.sql.SQLException;

public class DbPool {
	public static boolean isInitialized() {
		return jfxpossyn.util.DbPool.isInitialized();
	}

	public static void init(AppConfig config) {
		jfxpossyn.util.DbPool.init(config);
	}

	public static Connection getConnection() throws SQLException {
		if (!isInitialized()) {
			synchronized (DbPool.class) {
				if (!isInitialized()) {
					AppConfig config = AppConfigStore.load();
					init(config);
				}
			}
		}
		return jfxpossyn.util.DbPool.getConnection();
	}

	public static void close() {
		jfxpossyn.util.DbPool.close();
	}
}
