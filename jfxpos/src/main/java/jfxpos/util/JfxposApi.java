package jfxpos.util;

import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JfxposApi {

	private static String deviceCode = "";
	private static String apiKey = "";
	private static String secret = "";
	private static String siteCode = "";
	private static String structCode = "";

	static {
		try {
			AppConfig config = AppConfigStore.load();
			init(config);
		} catch (Exception e) {
			// Ignore
		}
	}

	public static void init(AppConfig config) {
		if (config != null) {
			init(config.deviceCode(), config.apiKey(), config.secret(), config.siteCode(), config.structCode());
		}
	}

	public static void init(String devCode, String key, String sec, String site, String struct) {
		deviceCode = devCode != null ? devCode : "";
		apiKey = key != null ? key : "";
		secret = sec != null ? sec : "";
		siteCode = site != null ? site : "";
		structCode = struct != null ? struct : "";
	}

	public static HttpResponse<String> get(String apiUrl) throws Exception {
		HttpRequest request = createRequestBuilder(apiUrl, "{}")
				.GET()
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> post(String apiUrl, String body) throws Exception {
		if (body == null) body = "";
		HttpRequest request = createRequestBuilder(apiUrl, body)
				.POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
				.header("Content-Type", "application/json")
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> put(String apiUrl, String body) throws Exception {
		if (body == null) body = "";
		HttpRequest request = createRequestBuilder(apiUrl, body)
				.PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
				.header("Content-Type", "application/json")
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> del(String apiUrl) throws Exception {
		HttpRequest request = createRequestBuilder(apiUrl, "{}")
				.DELETE()
				.build();
		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static HttpRequest.Builder createRequestBuilder(String apiUrl, String payload) throws Exception {
		String timestamp = Instant.now().toString();
		String signature = calculateHmacSha256(payload + timestamp, secret);

		return HttpRequest.newBuilder()
				.uri(URI.create(apiUrl))
				.header("X-Device-Code", deviceCode)
				.header("X-API-Key", apiKey)
				.header("X-Timestamp", timestamp)
				.header("X-Signature", signature)
				.header("X-Site-Code", siteCode)
				.header("X-Dept-Code", structCode);
	}

	public static String calculateHmacSha256(String data, String key) throws Exception {
		if (key == null) key = "";
		if (data == null) data = "";
		byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
		Mac sha256HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
		sha256HMAC.init(keySpec);
		byte[] macData = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
		StringBuilder result = new StringBuilder();
		for (byte b : macData) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}
}
