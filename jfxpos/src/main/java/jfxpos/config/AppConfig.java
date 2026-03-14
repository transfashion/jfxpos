package jfxpos.config;

public record AppConfig(
		String serverUrl,
		String siteId,
		String machineId,
		String key,
		String ticketPrinterName) {
}
