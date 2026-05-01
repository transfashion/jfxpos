package jfxpos;

import java.util.logging.Logger;

import javafx.stage.Stage;
import jfxpos.util.PosLogger;

public abstract class Controller {
	protected final Logger logger;
	protected Stage stage;

	protected Controller(Class<?> clazz) {
		this.logger = PosLogger.createLogger(clazz.getName());
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
