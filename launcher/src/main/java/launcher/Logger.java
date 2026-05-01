package launcher;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Logger {

    public static java.util.logging.Logger createLogger(String className) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(className);

        // 1. Matikan handler bawaan yang mengirim ke System.err
        logger.setUseParentHandlers(false);

        // 2. Buat handler baru yang mengirim ke System.out
        StreamHandler sh = new StreamHandler(System.out, new SimpleFormatter()) {
            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                flush(); // Pastikan pesan langsung muncul
            }
        };

        logger.addHandler(sh);

        return logger;
    }
}
