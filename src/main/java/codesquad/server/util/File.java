package codesquad.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class File {
    private static final Logger logger = LoggerFactory.getLogger(File.class);
    private static final String STATIC_PATH = "/static";

    private File() {
    }

    public static Map<String, java.io.File> loadStaticFiles() {
        Map<String, java.io.File> staticFiles = new HashMap<>();
        URL resourceUrl = File.class.getResource(STATIC_PATH);
        if (resourceUrl == null) {
            logger.error("Static resource directory not found");
            return staticFiles;
        }

        java.io.File resourceDir = new java.io.File(resourceUrl.getFile());
        if (!resourceDir.exists() || !resourceDir.isDirectory()) {
            logger.error("Static resource path is not a directory");
            return staticFiles;
        }

        loadFilesRecursively(resourceDir, "", staticFiles);
        return staticFiles;
    }

    private static void loadFilesRecursively(java.io.File directory, String path, Map<String, java.io.File> staticFiles) {
        java.io.File[] files = directory.listFiles();
        if (files == null) return;

        for (java.io.File file : files) {
            String currentPath = path + "/" + file.getName();
            if (file.isDirectory()) {
                loadFilesRecursively(file, currentPath, staticFiles);
            } else {
                staticFiles.put(currentPath, file);
            }
        }
    }

    public static byte[] readFileToByteArray(java.io.File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
}
