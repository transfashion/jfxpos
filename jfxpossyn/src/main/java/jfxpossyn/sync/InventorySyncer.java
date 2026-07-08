package jfxpossyn.sync;

import jfxpossyn.config.AppConfig;
import java.util.logging.Logger;

public class InventorySyncer {
	public static final String TITLE = "Synchronize Inventory";
	private static final Logger logger = Logger.getLogger(InventorySyncer.class.getName());

	/**
	 * Performs inventory synchronization using the provided application
	 * configuration.
	 *
	 * @param config The application configuration containing server URL, API key,
	 *               etc.
	 */
	public void syncInventory(AppConfig config) {
		syncInventory(config, null);
	}

	/**
	 * Performs inventory synchronization using the provided application
	 * configuration
	 * and reports progress.
	 *
	 * @param config   The application configuration containing server URL, API key,
	 *                 etc.
	 * @param listener The progress listener to notify.
	 */
	public void syncInventory(AppConfig config, SyncProgressListener listener) {
		if (config == null) {
			logger.warning("Synchronization aborted: AppConfig is null.");
			if (listener != null) {
				listener.onProgress(0.0, TITLE, "Synchronization aborted: AppConfig is null.");
			}
			return;
		}

		logger.info("Starting inventory synchronization...");
		if (listener != null) {
			listener.onProgress(0.0, TITLE, "Starting inventory synchronization...");
		}

		int totalSteps = 10;
		for (int i = 1; i <= totalSteps; i++) {
			try {
				Thread.sleep(500); // Simulate work
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				logger.warning("Synchronization interrupted.");
				if (listener != null) {
					listener.onProgress((double) i / totalSteps, TITLE, "Synchronization interrupted.");
				}
				return;
			}

			double progress = (double) i / totalSteps;
			String message = "Syncing inventory dari: step " + i + " of " + totalSteps + "...";
			// logger.info(message);
			if (listener != null) {
				listener.onProgress(progress, TITLE, message);
			}
		}

		logger.info("Inventory synchronization skeleton executed successfully.");
		if (listener != null) {
			listener.onProgress(1.0, TITLE, "Inventory synchronization completed successfully.");
		}
	}
}
