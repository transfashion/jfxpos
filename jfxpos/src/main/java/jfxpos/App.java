package jfxpos;

import javafx.application.Application;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.util.WindowManager;

import jfxpos.util.PosLogger;
import java.util.logging.Logger;

public class App extends Application {
	public static final boolean isDev = System.getProperty("app.env", "prod") == "prod" ? false : true;

	private static final Logger logger = PosLogger.createLogger(App.class.getName());

	public static AppConfig config;

	@Override
	public void start(Stage stage) throws Exception {
		WindowManager.setMainStage(stage);
		WindowManager.openLogin();
	}

	public static void readConfiguration() throws Exception {
		try {
			logger.info("Reading configuration...");
			App.config = AppConfigStore.load();
			logger.info("Configuration loaded!");
			Thread.sleep(500);
		} catch (Exception e) {
			throw e;
		}
	}

}
