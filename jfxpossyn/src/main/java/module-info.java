module jfxpossyn {
    requires transitive java.logging;
    requires java.prefs;
    requires transitive java.sql;
    requires java.net.http;
    requires org.firebirdsql.jaybird;
    requires com.zaxxer.hikari;

    exports jfxpossyn;
    exports jfxpossyn.sync;
    exports jfxpossyn.config;
    exports jfxpossyn.util;
    exports jfxpossyn.model;
    exports jfxpossyn.repository;
}
