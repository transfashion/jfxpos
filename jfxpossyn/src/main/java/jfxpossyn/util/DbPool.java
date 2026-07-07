package jfxpossyn.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jfxpossyn.config.AppConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbPool {
	private static final Logger logger = Logger.getLogger(DbPool.class.getName());
	private static HikariDataSource dataSource;

	public static synchronized boolean isInitialized() {
		return dataSource != null;
	}

	public static synchronized void init(AppConfig config) {
		if (dataSource != null) {
			logger.info("Closing existing database connection pool in jfxpossyn...");
			dataSource.close();
			dataSource = null;
		}

		if (config.databaseHost() == null || config.databaseHost().trim().isEmpty() ||
				config.databasePath() == null || config.databasePath().trim().isEmpty()) {
			logger.warning("Database configuration is incomplete. Skipping database pool initialization.");
			return;
		}

		try {
			HikariConfig hikariConfig = new HikariConfig();

			String host = config.databaseHost().trim();
			String path = config.databasePath().trim();
			String url;
			if (host.contains(":")) {
				url = "jdbc:firebirdsql://" + host + "/" + path;
			} else {
				url = "jdbc:firebirdsql://" + host + ":3050/" + path;
			}

			hikariConfig.setJdbcUrl(url);
			hikariConfig.setUsername(config.databaseUsername());
			hikariConfig.setPassword(config.databasePassword());
			if (config.databaseRole() != null && !config.databaseRole().trim().isEmpty()) {
				hikariConfig.addDataSourceProperty("roleName", config.databaseRole().trim());
			}

			// Force driver registration
			hikariConfig.setDriverClassName("org.firebirdsql.jdbc.FBDriver");

			// Connection Pool settings
			int poolSize = config.databasePoolSize();
			if (poolSize <= 0) {
				poolSize = 3;
			}
			hikariConfig.setMaximumPoolSize(poolSize);
			hikariConfig.setMinimumIdle(1);
			hikariConfig.setIdleTimeout(30000); // 30 seconds
			hikariConfig.setConnectionTimeout(5000); // 5 seconds connection timeout

			logger.info("Initializing HikariCP connection pool in jfxpossyn. URL: " + url + ", Max Pool Size: " + poolSize);
			dataSource = new HikariDataSource(hikariConfig);
			logger.info("HikariCP connection pool initialized successfully in jfxpossyn.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to initialize HikariCP connection pool", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			throw new SQLException("Database connection pool in jfxpossyn is not initialized. Please configure settings.");
		}
		return dataSource.getConnection();
	}

	public static synchronized void close() {
		if (dataSource != null) {
			logger.info("Closing HikariCP connection pool in jfxpossyn...");
			dataSource.close();
			dataSource = null;
			logger.info("HikariCP connection pool closed.");
		}
	}
}
