package launcher;

import java.nio.file.Path;
import java.util.function.Consumer;



public final class Updater {
	private static final java.util.logging.Logger logger = Logger.createLogger(Updater.class.getName());

	public static void update( Path jarPath, Consumer<String> progress) {
		try {

			String jarVersion = JarUtils.getVersion(jarPath);
			if (jarVersion == null) {
				logger.warning( "Jar version is null");
			} else {
				logger.info( "Jar version: " + jarVersion);
			}

			progress.accept("Check for update");
			UpdateInfo info = getUpdateInfo();

			if (info != null) {
				logger.info( "update ditemukan!");
				progress.accept("Download update");
				downloadUpdate(info);

				progress.accept("Apply update");
				updateJar(info);
			}

			progress.accept("Update complete");

		} catch (Exception e) {
			progress.accept("Error: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}


	static UpdateInfo getUpdateInfo() throws Exception {
		logger.info("cek metadata info update dari server");
		Thread.sleep(3000);


        return new UpdateInfo();
	}

	static void downloadUpdate(UpdateInfo info) throws Exception {
		logger.info( "download update");
		Thread.sleep(3000);
	}

	static void updateJar(UpdateInfo info) throws Exception {
		logger.info("update file jar dengan yang baru");
		Thread.sleep(3000);
	}
}
