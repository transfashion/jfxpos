package jfxpossyn.sync;

import jfxpossyn.config.AppConfig;
import jfxpossyn.model.Item;
import jfxpossyn.repository.ItemRepository;
import jfxpossyn.util.JsonParser;
import jfxpossyn.util.SignatureHelper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemSyncer {
	public static final String TITLE = "Synchronize Item";
	private static final Logger logger = Logger.getLogger(ItemSyncer.class.getName());
	private final ItemRepository itemRepository = new ItemRepository();

	/**
	 * Performs item synchronization using the provided application configuration.
	 *
	 * @param config The application configuration containing server URL, API key,
	 *               etc.
	 */
	public void syncItem(AppConfig config) {
		syncItem(config, null);
	}

	/**
	 * Performs item synchronization using the provided application configuration
	 * and reports progress.
	 *
	 * @param config   The application configuration containing server URL, API key,
	 *                 etc.
	 * @param listener The progress listener to notify.
	 */
	public void syncItem(AppConfig config, SyncProgressListener listener) {
		if (config == null) {
			logger.warning("Synchronization aborted: AppConfig is null.");
			if (listener != null) {
				listener.onProgress(0.0, TITLE, "Synchronization aborted: AppConfig is null.");
			}
			return;
		}

		logger.info("Starting item synchronization...");
		if (listener != null) {
			listener.onProgress(0.0, TITLE, "Mengambil datatimestamp terakhir...");
		}

		Integer possyncId = null;
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();

		try {
			// 1. Ambil datatimestamp terakhir dari tabel ITEM
			String lastTimestamp = itemRepository.getLastDataTimestamp();
			logger.info("Last item datatimestamp: " + lastTimestamp);

			if (listener != null) {
				listener.onProgress(0.1, TITLE, "Menghubungi server API (prepare)...");
			}

			// 2. Eksekusi API /api/possync/prepareItemSyn
			String prepareUrl = config.serverUrl() + "/api/possync/prepareItemSyn";
			String prepareBody = String.format(
					"{\"posdevice_id\":%d,\"client_timestamp\":\"%s\",\"datatimestamp\":\"%s\"}",
					config.deviceId(),
					DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
					lastTimestamp
			);

			HttpRequest prepareRequest = createRequest(prepareUrl, "POST", prepareBody, config);
			HttpResponse<String> prepareResponse = client.send(prepareRequest, HttpResponse.BodyHandlers.ofString());

			if (prepareResponse.statusCode() != 201 && prepareResponse.statusCode() != 200) {
				throw new Exception("Gagal prepare sync. HTTP status: " + prepareResponse.statusCode() + ", response: " + prepareResponse.body());
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> prepareMap = (Map<String, Object>) JsonParser.parse(prepareResponse.body());
			if (prepareMap == null || !Boolean.TRUE.equals(prepareMap.get("success"))) {
				throw new Exception("Prepare sync gagal: " + (prepareMap != null ? prepareMap.get("message") : "response kosong"));
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) prepareMap.get("data");
			if (data == null) {
				throw new Exception("Prepare sync data response kosong");
			}

			Object syncIdObj = data.get("possync_id");
			Object blockCountObj = data.get("blockcount");
			Object rowCountObj = data.get("rowcount");

			possyncId = syncIdObj instanceof Number ? ((Number) syncIdObj).intValue() : Integer.parseInt(syncIdObj.toString());
			int blockCount = blockCountObj instanceof Number ? ((Number) blockCountObj).intValue() : Integer.parseInt(blockCountObj.toString());
			int rowCount = rowCountObj instanceof Number ? ((Number) rowCountObj).intValue() : Integer.parseInt(rowCountObj.toString());

			logger.info(String.format("Prepared Sync ID: %d, Row Count: %d, Block Count: %d", possyncId, rowCount, blockCount));

			if (blockCount == 0) {
				logger.info("Tidak ada item baru yang perlu disinkronkan.");
				if (listener != null) {
					listener.onProgress(0.9, TITLE, "Tidak ada data item baru.");
				}
			} else {
				// 3. Loop blockcount sampai habis dengan memanggil /api/possync/getItemSync
				for (int block = 1; block <= blockCount; block++) {
					String msg = String.format("Mengunduh item blok %d/%d...", block, blockCount);
					logger.info(msg);
					if (listener != null) {
						double progress = 0.1 + ((double) block / blockCount) * 0.7;
						listener.onProgress(progress, TITLE, msg);
					}

					String getUrl = String.format("%s/api/possync/getItemSync?possync_id=%d&synblock=%d",
							config.serverUrl(), possyncId, block);

					HttpRequest getRequest = createRequest(getUrl, "GET", "", config);
					HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

					if (getResponse.statusCode() != 200) {
						throw new Exception("Gagal mengambil data block " + block + ". HTTP status: " + getResponse.statusCode());
					}

					@SuppressWarnings("unchecked")
					Map<String, Object> blockMap = (Map<String, Object>) JsonParser.parse(getResponse.body());
					if (blockMap == null || !Boolean.TRUE.equals(blockMap.get("success"))) {
						throw new Exception("Mengambil data block " + block + " gagal: " + (blockMap != null ? blockMap.get("message") : "response kosong"));
					}

					@SuppressWarnings("unchecked")
					List<Object> itemsList = (List<Object>) blockMap.get("data");
					if (itemsList != null && !itemsList.isEmpty()) {
						List<Item> itemsToSave = parseItems(itemsList);
						itemRepository.saveItems(itemsToSave);
						logger.info(String.format("Berhasil menyimpan %d item dari blok %d", itemsToSave.size(), block));
					}
				}
			}

			// 4. Selesaikan sinkronisasi dengan memanggil /api/possync/finishItemSync
			if (listener != null) {
				listener.onProgress(0.95, TITLE, "Menyelesaikan proses sinkronisasi...");
			}
			callFinishSync(client, config, possyncId, true, false, "");

			logger.info("Sinkronisasi item berhasil diselesaikan.");
			if (listener != null) {
				listener.onProgress(1.0, TITLE, "Sinkronisasi item berhasil diselesaikan.");
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Kesalahan saat melakukan sinkronisasi item", e);
			if (possyncId != null) {
				try {
					callFinishSync(client, config, possyncId, false, true, e.getMessage());
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Gagal melaporkan kesalahan sinkronisasi ke server", ex);
				}
			}
			
			Throwable cause = e;
			if (e instanceof java.util.concurrent.CompletionException && e.getCause() != null) {
				cause = e.getCause();
			}
			
			String userFriendlyMessage = cause.getMessage();
			if (cause instanceof java.net.ConnectException || cause.getClass().getName().contains("ConnectException")) {
				userFriendlyMessage = "Koneksi ke server gagal. Pastikan server aktif dan URL benar (" + config.serverUrl() + ").";
			} else if (cause instanceof java.net.http.HttpConnectTimeoutException) {
				userFriendlyMessage = "Koneksi ke server timeout. Pastikan jaringan stabil dan server aktif.";
			} else if (userFriendlyMessage == null || userFriendlyMessage.trim().isEmpty()) {
				userFriendlyMessage = cause.getClass().getSimpleName();
			}

			if (listener != null) {
				listener.onProgress(1.0, TITLE, "Sinkronisasi gagal: " + userFriendlyMessage);
			}
			// Throw exception to main sync executor with user-friendly message
			throw new RuntimeException(userFriendlyMessage, cause);
		}
	}

	private HttpRequest createRequest(String url, String method, String body, AppConfig config) {
		String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
		String payload = (body == null || body.isEmpty()) ? "{}" : body;
		String signature = SignatureHelper.calculateHmacSha256(payload + timestamp, config.secret());

		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.header("X-Device-Code", config.deviceCode() != null ? config.deviceCode() : "")
				.header("X-API-Key", config.apiKey() != null ? config.apiKey() : "")
				.header("X-Timestamp", timestamp)
				.header("X-Signature", signature)
				.header("X-Site-Code", config.siteCode() != null ? config.siteCode() : "")
				.header("X-Dept-Code", config.structCode() != null ? config.structCode() : "");

		if ("POST".equalsIgnoreCase(method)) {
			builder.POST(HttpRequest.BodyPublishers.ofString(body));
		} else {
			builder.GET();
		}
		return builder.build();
	}

	private void callFinishSync(HttpClient client, AppConfig config, int possyncId, boolean isCompleted, boolean isError, String errorMessage) throws Exception {
		String finishUrl = config.serverUrl() + "/api/possync/finishItemSync";
		String finishBody = String.format(
				"{\"possync_id\":%d,\"is_completed\":%b,\"is_error\":%b,\"errormessage\":\"%s\"}",
				possyncId,
				isCompleted,
				isError,
				errorMessage != null ? errorMessage.replace("\"", "\\\"") : ""
		);

		HttpRequest request = createRequest(finishUrl, "POST", finishBody, config);
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new Exception("Gagal finish sync. HTTP status: " + response.statusCode() + ", response: " + response.body());
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> respMap = (Map<String, Object>) JsonParser.parse(response.body());
		if (respMap == null || !Boolean.TRUE.equals(respMap.get("success"))) {
			throw new Exception("Finish sync gagal: " + (respMap != null ? respMap.get("message") : "response kosong"));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Item> parseItems(List<Object> rawList) {
		List<Item> items = new ArrayList<>();
		for (Object obj : rawList) {
			if (!(obj instanceof Map)) {
				continue;
			}
			Map<String, Object> m = (Map<String, Object>) obj;
			Item item = new Item();

			Object itemIdVal = m.get("item_id");
			if (itemIdVal != null) {
				item.setItemId(itemIdVal instanceof Number ? ((Number) itemIdVal).longValue() : Long.parseLong(itemIdVal.toString()));
			}

			Object codeVal = m.get("item_code");
			if (codeVal == null) codeVal = m.get("item_art");
			item.setItemArt(codeVal != null ? codeVal.toString() : "");

			Object nameVal = m.get("name");
			if (nameVal == null) nameVal = m.get("item_descr");
			item.setItemDescr(nameVal != null ? nameVal.toString() : "");

			Object colVal = m.get("item_col");
			item.setItemCol(colVal != null ? colVal.toString() : null);

			Object sizeVal = m.get("item_size");
			item.setItemSize(sizeVal != null ? sizeVal.toString() : null);

			Object priceVal = m.get("price");
			if (priceVal == null) priceVal = m.get("item_price");
			if (priceVal != null) {
				item.setItemPrice(new java.math.BigDecimal(priceVal.toString()));
			}

			Object priceGrossVal = m.get("item_pricegross");
			if (priceGrossVal == null) priceGrossVal = m.get("pricegross");
			if (priceGrossVal == null) priceGrossVal = priceVal;
			if (priceGrossVal != null) {
				item.setItemPriceGross(new java.math.BigDecimal(priceGrossVal.toString()));
			}

			Object discVal = m.get("item_disc");
			if (discVal == null) discVal = m.get("disc");
			if (discVal != null) {
				item.setItemDisc(new java.math.BigDecimal(discVal.toString()));
			}

			Object specPriceVal = m.get("item_isspecialprice");
			if (specPriceVal != null) {
				item.setItemIsSpecialPrice(Boolean.parseBoolean(specPriceVal.toString()));
			}

			Object disabledVal = m.get("item_isdisabled");
			if (disabledVal != null) {
				item.setItemIsDisabled(Boolean.parseBoolean(disabledVal.toString()));
			}

			Object ctgIdVal = m.get("ctg_id");
			if (ctgIdVal != null) {
				item.setCtgId(ctgIdVal instanceof Number ? ((Number) ctgIdVal).intValue() : Integer.parseInt(ctgIdVal.toString()));
			}
			Object ctgNameVal = m.get("ctg_name");
			item.setCtgName(ctgNameVal != null ? ctgNameVal.toString() : null);

			Object unitIdVal = m.get("unit_id");
			if (unitIdVal != null) {
				item.setUnitId(unitIdVal instanceof Number ? ((Number) unitIdVal).intValue() : Integer.parseInt(unitIdVal.toString()));
			}
			Object unitNameVal = m.get("unit_name");
			item.setUnitName(unitNameVal != null ? unitNameVal.toString() : null);

			Object structIdVal = m.get("struct_id");
			if (structIdVal != null) {
				item.setStructId(structIdVal instanceof Number ? ((Number) structIdVal).intValue() : Integer.parseInt(structIdVal.toString()));
			}
			Object structNameVal = m.get("struct_name");
			item.setStructName(structNameVal != null ? structNameVal.toString() : null);

			Object brandIdVal = m.get("brand_id");
			if (brandIdVal != null) {
				item.setBrandId(brandIdVal instanceof Number ? ((Number) brandIdVal).intValue() : Integer.parseInt(brandIdVal.toString()));
			}
			Object brandNameVal = m.get("brand_name");
			item.setBrandName(brandNameVal != null ? brandNameVal.toString() : null);

			Object dtTsVal = m.get("datatimestamp");
			if (dtTsVal != null) {
				try {
					Instant instant = Instant.parse(dtTsVal.toString());
					item.setDataTimestamp(java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault()));
				} catch (Exception e) {
					try {
						item.setDataTimestamp(java.time.LocalDateTime.parse(dtTsVal.toString()));
					} catch (Exception ex) {
						// Ignored
					}
				}
			}

			Object md5Val = m.get("md5hash");
			item.setMd5Hash(md5Val != null ? md5Val.toString() : null);

			items.add(item);
		}
		return items;
	}
}
