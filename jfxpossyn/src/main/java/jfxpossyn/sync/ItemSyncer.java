package jfxpossyn.sync;

import jfxpossyn.config.AppConfig;
import java.util.logging.Logger;

public class ItemSyncer {
    private static final Logger logger = Logger.getLogger(ItemSyncer.class.getName());

    /**
     * Performs item synchronization using the provided application configuration.
     *
     * @param config The application configuration containing server URL, API key, etc.
     */
    public void syncItem(AppConfig config) {
        syncItem(config, null);
    }

    /**
     * Performs item synchronization using the provided application configuration and reports progress.
     *
     * @param config   The application configuration containing server URL, API key, etc.
     * @param listener The progress listener to notify.
     */
    public void syncItem(AppConfig config, SyncProgressListener listener) {
        if (config == null) {
            logger.warning("Synchronization aborted: AppConfig is null.");
            if (listener != null) {
                listener.onProgress(0.0, "Synchronization aborted: AppConfig is null.");
            }
            return;
        }

        logger.info("Starting item synchronization...");
        if (listener != null) {
            listener.onProgress(0.0, "Starting item synchronization...");
        }

        int totalSteps = 10;
        for (int i = 1; i <= totalSteps; i++) {
            try {
                Thread.sleep(500); // Simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warning("Synchronization interrupted.");
                if (listener != null) {
                    listener.onProgress((double) i / totalSteps, "Synchronization interrupted.");
                }
                return;
            }

            double progress = (double) i / totalSteps;
            String message = "Syncing items: step " + i + " of " + totalSteps + "...";
            logger.info(message);
            if (listener != null) {
                listener.onProgress(progress, message);
            }
        }

        logger.info("Item synchronization skeleton executed successfully.");
        if (listener != null) {
            listener.onProgress(1.0, "Item synchronization completed successfully.");
        }
    }
}
