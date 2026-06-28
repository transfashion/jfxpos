package jfxpos.config;

import java.util.prefs.Preferences;

public class AppConfigStore {

	static final String SERVER_URL = "app.serverUrl";
	static final String SITE_ID = "app.siteId";
	static final String MACHINE_ID = "app.machineId";
	static final String KEY = "app.key";
	static final String TICKET_PRINTER_NAME = "app.ticketPrinterName";
	static final String LAST_USERNAME = "app.lastUsername";
	static final String DATABASE_HOST = "app.databaseHost";
	static final String DATABASE_PATH = "app.databasePath";
	static final String DATABASE_USERNAME = "app.databaseUsername";
	static final String DATABASE_PASSWORD = "app.databasePassword";
	static final String DATABASE_ROLE = "app.databaseRole";
	static final String DATABASE_POOL_SIZE = "app.databasePoolSize";

	private static final Preferences prefs = Preferences.userNodeForPackage(AppConfigStore.class);

	public static void save(AppConfig config) {
		prefs.put(SERVER_URL, config.serverUrl());
		prefs.put(SITE_ID, config.siteId());
		prefs.put(MACHINE_ID, config.machineId());
		prefs.put(KEY, config.secret());
		prefs.put(TICKET_PRINTER_NAME, config.ticketPrinterName());
		prefs.put(DATABASE_HOST, config.databaseHost());
		prefs.put(DATABASE_PATH, config.databasePath());
		prefs.put(DATABASE_USERNAME, config.databaseUsername());
		prefs.put(DATABASE_PASSWORD, config.databasePassword());
		prefs.put(DATABASE_ROLE, config.databaseRole());
		prefs.putInt(DATABASE_POOL_SIZE, config.databasePoolSize());
	}

	public static AppConfig load() {
		String serverUrl = prefs.get(SERVER_URL, "http://localhost");
		String siteId = prefs.get(SITE_ID, "");
		String machineId = prefs.get(MACHINE_ID, "");
		String key = prefs.get(KEY, "");
		String ticketPrinterName = prefs.get(TICKET_PRINTER_NAME, "");
		String databaseHost = prefs.get(DATABASE_HOST, "localhost");
		String databasePath = prefs.get(DATABASE_PATH, "");
		String databaseUsername = prefs.get(DATABASE_USERNAME, "");
		String databasePassword = prefs.get(DATABASE_PASSWORD, "");
		String databaseRole = prefs.get(DATABASE_ROLE, "");
		int databasePoolSize = prefs.getInt(DATABASE_POOL_SIZE, 3);
		return new AppConfig(serverUrl, siteId, machineId, key, ticketPrinterName,
				databaseHost, databasePath, databaseUsername, databasePassword, databaseRole, databasePoolSize);
	}

	public static String getLastUsername() {
		String username = prefs.get(LAST_USERNAME, "");
		return username;
	}

	public static void saveLastUsername(String username) {
		prefs.put(LAST_USERNAME, username);
	}
}
