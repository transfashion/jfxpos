package jfxpossyn.sync;

import jfxpossyn.config.AppConfig;
import jfxpossyn.model.Salesperson;
import jfxpossyn.repository.SalespersonRepository;
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

public class SalespersonSyncer {
	public static final String TITLE = "Synchronize Salesperson";
	private static final Logger logger = Logger.getLogger(SalespersonSyncer.class.getName());
	private final SalespersonRepository salespersonRepository = new SalespersonRepository();

	public void syncSalesperson(AppConfig config) {
		syncSalesperson(config, null);
	}

	public void syncSalesperson(AppConfig config, SyncProgressListener listener) {
		if (config == null) {
			logger.warning("Synchronization aborted: AppConfig is null.");
			if (listener != null) {
				listener.onProgress(0.0, TITLE, "Synchronization aborted: AppConfig is null.");
			}
			return;
		}

		logger.info("Starting salesperson synchronization...");
		if (listener != null) {
			listener.onProgress(0.0, TITLE, "Mengambil datatimestamp terakhir...");
		}

		Integer possyncId = null;
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();

		try {
			// 1. Ambil datatimestamp terakhir
			String lastTimestamp = salespersonRepository.getLastDataTimestamp();
			logger.info("Last salesperson datatimestamp: " + lastTimestamp);

			if (listener != null) {
				listener.onProgress(0.1, TITLE, "Menghubungi server API (prepare)...");
			}

			// 2. Eksekusi API prepare
			String prepareUrl = config.serverUrl() + "/api/possync/prepareSalespersonSyn";
			String prepareBody = String.format(
					"{\"posdevice_id\":%d,\"client_timestamp\":\"%s\",\"datatimestamp\":\"%s\",\"site_id\":%d}",
					config.deviceId(),
					DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
					lastTimestamp,
					config.siteId() != null ? config.siteId() : 0);

			HttpRequest prepareRequest = createRequest(prepareUrl, "POST", prepareBody, config);
			HttpResponse<String> prepareResponse = client.send(prepareRequest, HttpResponse.BodyHandlers.ofString());

			if (prepareResponse.statusCode() != 201 && prepareResponse.statusCode() != 200) {
				throw new Exception("Gagal prepare sync salesperson. HTTP status: " + prepareResponse.statusCode() + ", response: "
						+ prepareResponse.body());
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> prepareMap = (Map<String, Object>) JsonParser.parse(prepareResponse.body());
			if (prepareMap == null || !Boolean.TRUE.equals(prepareMap.get("success"))) {
				throw new Exception(
						"Prepare sync salesperson gagal: " + (prepareMap != null ? prepareMap.get("message") : "response kosong"));
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) prepareMap.get("data");
			if (data == null) {
				throw new Exception("Prepare sync data response kosong");
			}

			Object syncIdObj = data.get("possync_id");
			Object blockCountObj = data.get("blockcount");
			Object rowCountObj = data.get("rowcount");

			possyncId = syncIdObj instanceof Number ? ((Number) syncIdObj).intValue()
					: Integer.parseInt(syncIdObj.toString());
			int blockCount = blockCountObj instanceof Number ? ((Number) blockCountObj).intValue()
					: Integer.parseInt(blockCountObj.toString());
			int rowCount = rowCountObj instanceof Number ? ((Number) rowCountObj).intValue()
					: Integer.parseInt(rowCountObj.toString());

			logger.info(String.format("Prepared Sync ID: %d, Row Count: %d, Block Count: %d", possyncId, rowCount,
					blockCount));

			if (blockCount == 0) {
				logger.info("Tidak ada data salesperson baru yang perlu disinkronkan.");
				if (listener != null) {
					listener.onProgress(0.9, TITLE, "Tidak ada data salesperson baru.");
				}
			} else {
				// 3. Loop blockcount
				for (int block = 1; block <= blockCount; block++) {
					String msg = String.format("Mengunduh data salesperson blok %d/%d...", block, blockCount);
					if (listener != null) {
						double progress = 0.1 + ((double) block / blockCount) * 0.7;
						listener.onProgress(progress, TITLE, msg);
					}

					String getUrl = String.format("%s/api/possync/getSalespersonSync?possync_id=%d&synblock=%d",
							config.serverUrl(), possyncId, block);

					HttpRequest getRequest = createRequest(getUrl, "GET", "", config);
					HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

					if (getResponse.statusCode() != 200) {
						throw new Exception(
								"Gagal mengambil data block " + block + ". HTTP status: " + getResponse.statusCode());
					}

					@SuppressWarnings("unchecked")
					Map<String, Object> blockMap = (Map<String, Object>) JsonParser.parse(getResponse.body());
					if (blockMap == null || !Boolean.TRUE.equals(blockMap.get("success"))) {
						throw new Exception("Mengambil data block " + block + " gagal: "
								+ (blockMap != null ? blockMap.get("message") : "response kosong"));
					}

					@SuppressWarnings("unchecked")
					List<Object> salespersonsList = (List<Object>) blockMap.get("data");
					if (salespersonsList != null && !salespersonsList.isEmpty()) {
						List<Salesperson> spToSave = parseSalespersons(salespersonsList);
						salespersonRepository.saveSalespersons(spToSave);
					}
				}
			}

			// 4. Selesaikan sinkronisasi
			if (listener != null) {
				listener.onProgress(0.95, TITLE, "Menyelesaikan proses sinkronisasi...");
			}
			callFinishSync(client, config, possyncId, true, false, "");

			logger.info("Sinkronisasi salesperson berhasil diselesaikan.");
			if (listener != null) {
				listener.onProgress(1.0, TITLE, "Sinkronisasi salesperson berhasil diselesaikan.");
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Kesalahan saat melakukan sinkronisasi salesperson", e);
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
				userFriendlyMessage = "Koneksi ke server gagal. Pastikan server aktif dan URL benar ("
						+ config.serverUrl() + ").";
			} else if (cause instanceof java.net.http.HttpConnectTimeoutException) {
				userFriendlyMessage = "Koneksi ke server timeout. Pastikan jaringan stabil dan server aktif.";
			} else if (userFriendlyMessage == null || userFriendlyMessage.trim().isEmpty()) {
				userFriendlyMessage = cause.getClass().getSimpleName();
			}

			if (listener != null) {
				listener.onProgress(1.0, TITLE, "Sinkronisasi gagal: " + userFriendlyMessage);
			}
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

	private void callFinishSync(HttpClient client, AppConfig config, int possyncId, boolean isCompleted,
			boolean isError, String errorMessage) throws Exception {
		String finishUrl = config.serverUrl() + "/api/possync/finishSalespersonSync";
		String finishBody = String.format(
				"{\"possync_id\":%d,\"is_completed\":%b,\"is_error\":%b,\"errormessage\":\"%s\"}",
				possyncId,
				isCompleted,
				isError,
				errorMessage != null ? errorMessage.replace("\"", "\\\"") : "");

		HttpRequest request = createRequest(finishUrl, "POST", finishBody, config);
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new Exception(
					"Gagal finish sync salesperson. HTTP status: " + response.statusCode() + ", response: " + response.body());
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> respMap = (Map<String, Object>) JsonParser.parse(response.body());
		if (respMap == null || !Boolean.TRUE.equals(respMap.get("success"))) {
			throw new Exception("Finish sync salesperson gagal: " + (respMap != null ? respMap.get("message") : "response kosong"));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Salesperson> parseSalespersons(List<Object> rawList) {
		List<Salesperson> list = new ArrayList<>();
		for (Object obj : rawList) {
			if (!(obj instanceof Map)) {
				continue;
			}
			Map<String, Object> m = (Map<String, Object>) obj;
			Salesperson sp = new Salesperson();

			Object idVal = m.get("salesperson_id");
			if (idVal != null) {
				sp.setSalespersonId(idVal instanceof Number ? ((Number) idVal).intValue()
						: Integer.parseInt(idVal.toString()));
			}

			Object nikVal = m.get("salesperson_nik");
			sp.setSalespersonNik(nikVal != null ? nikVal.toString() : "");

			Object nameVal = m.get("salesperson_name");
			sp.setSalespersonName(nameVal != null ? nameVal.toString() : "");

			Object disabledVal = m.get("salesperson_isdisabled");
			if (disabledVal != null) {
				sp.setSalespersonIsDisabled(Boolean.parseBoolean(disabledVal.toString()));
			}

			Object brandIdVal = m.get("brand_id");
			if (brandIdVal != null) {
				sp.setBrandId(brandIdVal instanceof Number ? ((Number) brandIdVal).intValue()
						: Integer.parseInt(brandIdVal.toString()));
			}

			Object siteIdVal = m.get("site_id");
			if (siteIdVal != null) {
				sp.setSiteId(siteIdVal instanceof Number ? ((Number) siteIdVal).intValue()
						: Integer.parseInt(siteIdVal.toString()));
			}

			Object dtTsVal = m.get("datatimestamp");
			if (dtTsVal != null) {
				try {
					Instant instant = Instant.parse(dtTsVal.toString());
					sp.setDataTimestamp(java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault()));
				} catch (Exception e) {
					try {
						sp.setDataTimestamp(java.time.LocalDateTime.parse(dtTsVal.toString()));
					} catch (Exception ex) {
						// Ignored
					}
				}
			}

			Object md5Val = m.get("md5hash");
			sp.setMd5Hash(md5Val != null ? md5Val.toString() : null);

			list.add(sp);
		}
		return list;
	}
}
