package jfxpossyn;

import jfxpossyn.config.AppConfig;
import jfxpossyn.model.SyncItem;
import jfxpossyn.repository.SyncItemRepository;
import jfxpossyn.sync.SyncProgressListener;
import jfxpossyn.sync.ItemSyncer;
import jfxpossyn.sync.InventorySyncer;
import jfxpossyn.sync.PromoSyncer;
import jfxpossyn.sync.PaymSyncer;
import jfxpossyn.util.DbPool;

import java.time.Duration;
import java.time.LocalDateTime;

public class UpdateData {

	// Variable / Konfigurasi interval sync minimum (dalam menit)
	private static final long MIN_SYNC_INTERVAL_MINUTES = 5;

	/**
	 * Starts the sequential synchronization process for Items, Inventory, Promos,
	 * and Payments,
	 * dividing progress equally among them.
	 *
	 * @param config   The application configuration.
	 * @param listener The progress listener to notify.
	 * @return true if synchronization actually ran, false if skipped because it is up-to-date (less than 5 minutes since last sync).
	 * @throws Exception If any synchronization step fails.
	 */
	public boolean start(AppConfig config, SyncProgressListener listener) throws Exception {
		// Pastikan pool database sudah diinisialisasi
		if (!DbPool.isInitialized()) {
			DbPool.init(config);
		}

		SyncItemRepository syncItemRepository = new SyncItemRepository();
		SyncItem latest = syncItemRepository.getLatestSyncItem();
		LocalDateTime now = LocalDateTime.now();

		if (latest != null && latest.getDatetime() != null) {
			long minutesSinceLastSync = Duration.between(latest.getDatetime(), now).toMinutes();
			if (minutesSinceLastSync < MIN_SYNC_INTERVAL_MINUTES) {
				if (listener != null) {
					listener.onProgress(1.0, "Update Data",
							"Data up-to-date (terakhir disinkronisasi " + minutesSinceLastSync + " menit yang lalu).");
				}
				return false;
			}
		}

		// Mulai pencatatan sync baru
		SyncItem currentSync = new SyncItem();
		currentSync.setDatetime(now);
		currentSync.setClearon(now.toLocalDate().plusDays(5));
		currentSync.setCompleted(false);
		int syncItemId = syncItemRepository.insertSyncItem(currentSync);
		long startTime = System.currentTimeMillis();

		try {
			// 1. ItemSyncer (0.0 to 0.25)
			ItemSyncer itemSyncer = new ItemSyncer();
			itemSyncer.syncItem(config, (progress, title, message) -> {
				if (listener != null) {
					double overallProgress = 0.0 + (progress * 0.25);
					listener.onProgress(overallProgress, title, message);
				}
			});

			// 2. InventorySyncer (0.25 to 0.50)
			InventorySyncer inventorySyncer = new InventorySyncer();
			inventorySyncer.syncInventory(config, (progress, title, message) -> {
				if (listener != null) {
					double overallProgress = 0.25 + (progress * 0.25);
					listener.onProgress(overallProgress, title, message);
				}
			});

			// 3. PromoSyncer (0.50 to 0.75)
			PromoSyncer promoSyncer = new PromoSyncer();
			promoSyncer.syncPromo(config, (progress, title, message) -> {
				if (listener != null) {
					double overallProgress = 0.50 + (progress * 0.25);
					listener.onProgress(overallProgress, title, message);
				}
			});

			// 4. PaymSyncer (0.75 to 1.00)
			PaymSyncer paymSyncer = new PaymSyncer();
			paymSyncer.syncPaym(config, (progress, title, message) -> {
				if (listener != null) {
					double overallProgress = 0.75 + (progress * 0.25);
					listener.onProgress(overallProgress, title, message);
				}
			});

			// Jika seluruh proses berhasil, tandai sebagai komplit dan catat durasi
			if (syncItemId != -1) {
				long endTime = System.currentTimeMillis();
				long durationMillis = endTime - startTime;
				// Pembulatan ke atas untuk durasi menit
				int durationMinutes = (int) Math.ceil(durationMillis / 60000.0);

				currentSync.setId(syncItemId);
				currentSync.setCompleted(true);
				currentSync.setCompletedDate(LocalDateTime.now());
				currentSync.setDuration(durationMinutes);
				syncItemRepository.updateSyncItem(currentSync);
			}
		} catch (Exception e) {
			if (syncItemId != -1) {
				currentSync.setId(syncItemId);
				currentSync.setError(true);
				currentSync.setErrorMessage(e.getMessage());
				try {
					syncItemRepository.updateSyncItem(currentSync);
				} catch (Exception ex) {
					// Ignore database update exception to allow the original sync exception to propagate
				}
			}
			throw e;
		}

		return true;
	}
}
