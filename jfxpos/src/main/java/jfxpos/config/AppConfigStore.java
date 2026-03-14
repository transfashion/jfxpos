package jfxpos.config;

import java.util.prefs.Preferences;

public class AppConfigStore {

	static final String SERVER_URL = "app.serverUrl";
	static final String SITE_ID = "app.siteId";
	static final String MACHINE_ID = "app.machineId";
	static final String KEY = "app.key";
	static final String TICKET_PRINTER_NAME = "app.ticketPrinterName";
	static final String LAST_USERNAME = "app.lastUsername";

	private static final Preferences prefs = Preferences.userNodeForPackage(AppConfigStore.class);

	public static void save(AppConfig config) {
		prefs.put(SERVER_URL, config.serverUrl());
		prefs.put(SITE_ID, config.siteId());
		prefs.put(MACHINE_ID, config.machineId());
		prefs.put(KEY, config.key());
		prefs.put(TICKET_PRINTER_NAME, config.ticketPrinterName());

	}

	public static AppConfig load() {
		String serverUrl = prefs.get(SERVER_URL, "http://localhost");
		String siteId = prefs.get(SITE_ID, "");
		String machineId = prefs.get(MACHINE_ID, "");
		String key = prefs.get(KEY, "");
		String ticketPrinterName = prefs.get(TICKET_PRINTER_NAME, "");
		return new AppConfig(serverUrl, siteId, machineId, key, ticketPrinterName);
	}

	public static String getLastUsername() {
		String username = prefs.get(LAST_USERNAME, "");
		return username;
	}

	public static void saveLastUsername(String username) {
		prefs.put(LAST_USERNAME, username);
	}
}
