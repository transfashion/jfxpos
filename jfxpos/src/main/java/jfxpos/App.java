package jfxpos;

import javafx.application.Application;
import javafx.stage.Stage;
import jfxpos.config.AppConfig;
import jfxpos.config.AppConfigStore;

public class App extends Application {

	public static AppConfig config;

	@Override
	public void start(Stage stage) throws Exception {
		try {
			WindowManager.openDashboardWindow(stage);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void readConfiguration() throws Exception {
		try {
			System.out.println("Reading configuration...");
			App.config = AppConfigStore.load();
			System.out.println("Configuration loaded!");
			Thread.sleep(500);
		} catch (Exception e) {
			throw e;
		}
	}

}
