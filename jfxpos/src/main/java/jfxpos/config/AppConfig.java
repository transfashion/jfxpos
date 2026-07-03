package jfxpos.config;

public record AppConfig(
		String serverUrl,
		String apiKey,
		String siteCode,
		Integer siteId,
		String siteName,
		String structCode,
		Integer structId,
		String deviceCode,
		Integer deviceId,
		String name,
		String secret,
		String ticketPrinterName,
		String databaseHost,
		String databasePath,
		String databaseUsername,
		String databasePassword,
		String databaseRole,
		int databasePoolSize) {
}
