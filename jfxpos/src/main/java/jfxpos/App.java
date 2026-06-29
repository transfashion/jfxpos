package jfxpos;

import javafx.application.Application;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;
import jfxpos.util.PosLogger;
import jfxpos.views.DashboardWindow;

import java.util.Objects;
import java.util.logging.Logger;

public class App extends Application {
	public static final boolean isDev = !Objects.equals(System.getProperty("app.env", "prod"), "prod");
	public static final boolean isProd = Objects.equals(System.getProperty("app.env", "prod"), "prod");

	private static final Logger logger = PosLogger.createLogger(App.class.getName());

	public static AppConfig config;

	@Override
	public void start(Stage stage) throws Exception {
		DashboardWindow wnd = new DashboardWindow(stage);
		wnd.setLoginView();
		wnd.open();
	}

	// jangan dihapus, ini dipakai di launcher
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
