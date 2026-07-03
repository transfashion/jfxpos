module jfxpos {
	requires transitive javafx.controls;
	requires transitive javafx.fxml;

	requires transitive java.logging;

	requires java.prefs;
	requires java.sql;
	requires java.net.http;
	requires org.firebirdsql.jaybird;
	requires com.zaxxer.hikari;

	exports jfxpos;
	exports jfxpos.config;

	// Semua package yang digunakan FXML harus di 'opens', bukan 'exports'
	opens jfxpos.config to javafx.fxml;
	opens jfxpos.controller to javafx.fxml;
	opens jfxpos.util to javafx.fxml;
	opens jfxpos.models to javafx.base, javafx.fxml;
}