module launcher {
	requires transitive javafx.controls;

	requires javafx.fxml;
	requires java.prefs;

	requires jfxpos;
    requires transitive java.logging;

    exports launcher;
}