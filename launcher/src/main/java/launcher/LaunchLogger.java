package launcher;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import java.util.logging.StreamHandler;

public class LaunchLogger {

	public static java.util.logging.Logger createLogger(String className) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);

		// 1. Matikan handler bawaan yang mengirim ke System.err
		logger.setUseParentHandlers(false);

		// 2. Buat handler baru yang mengirim ke System.out
		StreamHandler sh = new StreamHandler(System.out, new Formatter() {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
					.withZone(ZoneId.systemDefault());

			@Override
			public String format(LogRecord record) {
				String time = dtf.format(Instant.ofEpochMilli(record.getMillis()));

				if (launcher.Launcher.isDev) {
					Level level = record.getLevel();
					if (level == Level.WARNING || level == Level.SEVERE) {
						return String.format(
								"[%s] %s.%s - %s%n",
								record.getLevel(),
								record.getSourceClassName(),
								record.getSourceMethodName(),
								record.getMessage());

					} else {
						return String.format(
								"[%s] %s%n",
								level,
								record.getMessage());
					}

				} else {
					return String.format(
							"[%s] [%s] [%s.%s] %s%n",
							time,
							record.getLevel(),
							record.getSourceClassName(),
							record.getSourceMethodName(),
							record.getMessage());
				}

			}
		})

		{

			@Override
			public synchronized void publish(LogRecord record) {
				super.publish(record);
				flush(); // Pastikan pesan langsung muncul
			}
		};

		// jika di dev environtment, tampilkan level FINE (Debug)
		// if (launcher.Launcher.isDev) {
		// logger.setLevel(java.util.logging.Level.FINE);
		// sh.setLevel(java.util.logging.Level.FINE);
		// }

		if (logger.getHandlers().length == 0) {
			logger.addHandler(sh);
		}

		return logger;
	}
}
