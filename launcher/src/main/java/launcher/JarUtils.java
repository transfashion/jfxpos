package launcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes;

public class JarUtils {

    public static String getVersion(Path jarPath) {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            Manifest manifest = jar.getManifest();
            if (manifest == null) return null;

            Attributes attrs = manifest.getMainAttributes();

            // urutan prioritas (umum dipakai)
            String version = attrs.getValue("Implementation-Version");
            if (version == null)
                version = attrs.getValue("Specification-Version");
            if (version == null)
                version = attrs.getValue("Bundle-Version");

            return version;
        } catch (IOException e) {
            throw new RuntimeException("Gagal membaca JAR: " + jarPath, e);
        }
    }
}