package launcher;

import java.util.function.Consumer;

public final class Updater {

	public static void update(Consumer<String> progress) {
		try {
			progress.accept("Check for update");
			UpdateInfo info = getUpdateInfo();

			if (info != null) {
				System.out.println("update ditemukan!");
				progress.accept("Download update");
				downloadUpdate(info);

				progress.accept("Apply update");
				updateJar(info);
			} else {
				System.out.println("Update tidak ditemukan.");
			}

			progress.accept("Update complete");

		} catch (Exception e) {
			progress.accept("Error: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}


	static UpdateInfo getUpdateInfo() throws Exception {
		System.out.println("cek metadata info update dari server, apakah ada pembaruan jar?");
		Thread.sleep(3000);
//		return null;

        return new UpdateInfo();
	}

	static void downloadUpdate(UpdateInfo info) throws Exception {
		System.out.println("download file jar dan meta dari server");
		Thread.sleep(3000);
	}

	static void updateJar(UpdateInfo info) throws Exception {
		System.out.println("update file jar dengan yang baru");
		Thread.sleep(3000);
	}
}
