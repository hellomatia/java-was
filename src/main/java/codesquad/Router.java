package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String RESOURCES_STATIC_PATH = "src/main/resources/static";
    private static final String STATIC_PATH = "/static";
    private final Map<String, String> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
        loadStaticFiles();
    }

    private void loadStaticFiles() {
        URL resourceUrl = getClass().getResource(STATIC_PATH);
        if (resourceUrl == null) {
            logger.error("Static resource directory not found");
            return;
        }

        File resourceDir = new File(resourceUrl.getFile());
        if (!resourceDir.exists() || !resourceDir.isDirectory()) {
            logger.error("Static resource path is not a directory");
            return;
        }

        loadFilesRecursively(resourceDir, "");
    }

    private void loadFilesRecursively(File directory, String path) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            String currentPath = path + "/" + file.getName();
            if (file.isDirectory()) {
                loadFilesRecursively(file, currentPath);
            } else {
                addRoute(currentPath, RESOURCES_STATIC_PATH + currentPath);
            }
        }
    }

    public void addRoute(String url, String filePath) {
        routes.put(url, filePath);
    }

    public String getFilePath(String url) {
        return routes.getOrDefault(url, "<h1>404 Not Found</h1>");
    }
}
