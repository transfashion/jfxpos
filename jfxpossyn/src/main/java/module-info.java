module jfxpossyn {
    requires transitive java.logging;
    requires java.prefs;
    requires java.sql;
    requires java.net.http;

    exports jfxpossyn;
    exports jfxpossyn.sync;
    exports jfxpossyn.config;
}
