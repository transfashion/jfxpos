package launcher;

import java.util.ResourceBundle;

public class Config {

	// ambil data dari resources/config.properties
	public static final ResourceBundle rb = ResourceBundle.getBundle("config");

	public static final String TITLE = rb.getString("title");
	public static final String ICON = rb.getString("icon");
	public static final String MODULE_NAME = rb.getString("module_to_load");

}
