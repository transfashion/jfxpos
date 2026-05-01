package launcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import java.util.logging.Logger;

public final class Updater {
	private static final Logger logger = LaunchLogger.createLogger(Updater.class.getName());

	public static void update(Path jarPath, Consumer<String> progress) {
		try {

			// catatan mengenai proses update:
			// update dilakukan di jfxpos.jar (cek secara periode, dan download di
			// background)
			// simpan hasilnya di file json dengan data
			// nomor versi, dan md5checksum
			progress.accept("Check for update");
			UpdateInfo info = getUpdateInfo(jarPath);

			if (info != null) {
				progress.accept("Apply update");
				updateJar(info);
			}

			progress.accept("Update complete");

		} catch (Exception e) {
			progress.accept("Error: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	static UpdateInfo getUpdateInfo(Path jarPath) throws Exception {
		logger.info("cek metadata info update dari server");
		Thread.sleep(3000);

		String currentJarVersion = JarUtils.getVersion(jarPath);
		if (currentJarVersion == null) {
			logger.warning("Cannot get current jar version");
			currentJarVersion = "0.0.0";
		} else {
			logger.info("Current jar version: " + currentJarVersion);
		}

		// direktori
		Path directory = jarPath.getParent();
		// directory.

		Path updateInfoPath = Paths.get(directory.toString(), "update", Config.MODULE_NAME + ".json").toAbsolutePath();
		logger.info("cek update info: " + updateInfoPath.toString());

		if (!Files.exists(updateInfoPath)) {
			return null;
		}

		return new UpdateInfo();
	}

	static void downloadUpdate(UpdateInfo info) throws Exception {
		logger.info("download update");
		Thread.sleep(3000);
	}

	static void updateJar(UpdateInfo info) throws Exception {
		logger.info("update file jar dengan yang baru");
		Thread.sleep(3000);
	}
}
