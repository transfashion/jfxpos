module launcher {
	requires transitive javafx.controls;

	requires java.prefs;
	requires transitive java.logging;
	requires java.sql;
	requires java.net.http;
	requires org.firebirdsql.jaybird;
	requires com.zaxxer.hikari;
	requires javafx.fxml;

    exports launcher;
}