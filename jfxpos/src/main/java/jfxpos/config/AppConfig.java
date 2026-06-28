package jfxpos.config;

public record AppConfig(
		String serverUrl,
		String siteId,
		String machineId,
		String secret,
		String ticketPrinterName,
		String databaseHost,
		String databasePath,
		String databaseUsername,
		String databasePassword,
		String databaseRole,
		int databasePoolSize) {
}
