package codesquad.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class File {
    private static final Logger logger = LoggerFactory.getLogger(File.class);
    private static final String STATIC_PATH = "static/";

    private File() {
    }

    public static Map<String, byte[]> loadStaticFiles() {
        Map<String, byte[]> staticFiles = new HashMap<>();
        URL url = File.class.getClassLoader().getResource(STATIC_PATH);

        if (url == null) {
            logger.error("Static resource directory not found");
            return staticFiles;
        }

        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            logger.error("Invalid URI for static resources", e);
            return staticFiles;
        }

        String protocol = uri.getScheme();
        if ("jar".equals(protocol)) {
            loadFromJar(staticFiles);
        } else {
            loadFromFileSystem(new java.io.File(uri), staticFiles);
        }

        logger.info("Loaded {} static files", staticFiles);
        return staticFiles;
    }

    private static void loadFromJar(Map<String, byte[]> staticFiles) {
        try {
            URI uri = File.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            try (JarFile jarFile = new JarFile(new java.io.File(uri))) {
                jarFile.entries().asIterator().forEachRemaining(entry -> {
                    if (!entry.isDirectory() && entry.getName().startsWith(STATIC_PATH)) {
                        try (InputStream is = jarFile.getInputStream(entry)) {
                            String key = entry.getName().substring(STATIC_PATH.length());
                            if (key.isEmpty()) {
                                key = "/";
                            } else if (!key.startsWith("/")) {
                                key = "/" + key;
                            }
                            staticFiles.put(key, readInputStreamToByteArray(is));
                        } catch (IOException e) {
                            logger.error("Error reading file from JAR: {}", entry.getName(), e);
                        }
                    }
                });
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error reading JAR file", e);
        }
    }

    private static void loadFromFileSystem(java.io.File directory, Map<String, byte[]> staticFiles) {
        loadFromFileSystemRecursively(directory, "", staticFiles);
    }

    private static void loadFromFileSystemRecursively(java.io.File directory, String path, Map<String, byte[]> staticFiles) {
        java.io.File[] files = directory.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                String relativePath = path + "/" + file.getName();
                if (file.isDirectory()) {
                    loadFromFileSystemRecursively(file, relativePath, staticFiles);
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        staticFiles.put(relativePath, readInputStreamToByteArray(fis));
                    } catch (IOException e) {
                        logger.error("Error reading file: {}", file.getPath(), e);
                    }
                }
            }
        }
    }

    public static byte[] readInputStreamToByteArray(InputStream is) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
}
