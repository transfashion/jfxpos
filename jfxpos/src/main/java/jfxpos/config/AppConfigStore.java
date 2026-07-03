package jfxpos.config;

import java.util.prefs.Preferences;

public class AppConfigStore {

	static final String SERVER_URL = "app.serverUrl";
	static final String API_KEY = "app.apiKey";
	static final String SITE_CODE = "app.siteCode";
	static final String STRUCT_CODE = "app.structCode";
	static final String DEVICE_CODE = "app.deviceCode";
	static final String SITE_ID = "app.siteId";
	static final String SITE_NAME = "app.siteName";
	static final String STRUCT_ID = "app.structId";
	static final String DEVICE_ID = "app.deviceId";
	static final String NAME = "app.name";
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
		prefs.put(API_KEY, config.apiKey());
		prefs.put(SITE_CODE, config.siteCode());
		prefs.put(STRUCT_CODE, config.structCode());
		prefs.put(DEVICE_CODE, config.deviceCode());
		if (config.siteId() != null) {
			prefs.putInt(SITE_ID, config.siteId());
		} else {
			prefs.remove(SITE_ID);
		}
		prefs.put(SITE_NAME, config.siteName() != null ? config.siteName() : "");
		if (config.structId() != null) {
			prefs.putInt(STRUCT_ID, config.structId());
		} else {
			prefs.remove(STRUCT_ID);
		}
		if (config.deviceId() != null) {
			prefs.putInt(DEVICE_ID, config.deviceId());
		} else {
			prefs.remove(DEVICE_ID);
		}
		prefs.put(NAME, config.name() != null ? config.name() : "");
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
		String apiKey = prefs.get(API_KEY, "");
		String siteCode = prefs.get(SITE_CODE, prefs.get("app.siteId", ""));
		String structCode = prefs.get(STRUCT_CODE, "");
		String deviceCode = prefs.get(DEVICE_CODE, prefs.get("app.machineId", ""));
		Integer siteId = prefs.get(SITE_ID, null) != null ? prefs.getInt(SITE_ID, 0) : null;
		String siteName = prefs.get(SITE_NAME, "");
		Integer structId = prefs.get(STRUCT_ID, null) != null ? prefs.getInt(STRUCT_ID, 0) : null;
		Integer deviceId = prefs.get(DEVICE_ID, null) != null ? prefs.getInt(DEVICE_ID, 0) : null;
		String name = prefs.get(NAME, "");
		String key = prefs.get(KEY, "");
		String ticketPrinterName = prefs.get(TICKET_PRINTER_NAME, "");
		String databaseHost = prefs.get(DATABASE_HOST, "localhost");
		String databasePath = prefs.get(DATABASE_PATH, "");
		String databaseUsername = prefs.get(DATABASE_USERNAME, "");
		String databasePassword = prefs.get(DATABASE_PASSWORD, "");
		String databaseRole = prefs.get(DATABASE_ROLE, "");
		int databasePoolSize = prefs.getInt(DATABASE_POOL_SIZE, 3);
		return new AppConfig(serverUrl, apiKey, siteCode, siteId, siteName, structCode, structId, deviceCode, deviceId, name, key, ticketPrinterName,
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
