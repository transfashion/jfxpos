module jfxpos {
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires java.prefs;

	exports jfxpos;

	// Semua package yang digunakan FXML harus di 'opens', bukan 'exports'
	opens jfxpos.config to javafx.fxml;
	opens jfxpos.controller to javafx.fxml;
	opens jfxpos.util to javafx.fxml;
}