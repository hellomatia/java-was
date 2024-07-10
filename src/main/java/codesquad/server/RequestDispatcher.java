package codesquad.server;

import codesquad.server.handler.RequestHandler;
import codesquad.server.handler.StaticFileHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RequestDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static RequestDispatcher instance;
    private final List<RequestHandler> requestHandlers;
    private final StaticFileHandler staticFileHandler;

    private RequestDispatcher() {
        requestHandlers = new CopyOnWriteArrayList<>();
        staticFileHandler = new StaticFileHandler();
        registerRequestHandlers();
        logger.debug("requestHandlers: {}", requestHandlers);
    }

    public static synchronized RequestDispatcher getInstance() {
        if (instance == null) {
            instance = new RequestDispatcher();
        }
        return instance;
    }

    private void registerRequestHandlers() {
        try {
            String packageName = "codesquad"; // 최상위 패키지로 변경
            String packagePath = packageName.replace(".", "/");
            logger.debug("Searching for handlers in package and subpackages: {}", packageName);

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = RequestDispatcher.class.getClassLoader();
            }

            Enumeration<URL> resources = classLoader.getResources(packagePath);
            if (!resources.hasMoreElements()) {
                logger.warn("No resources found for package: {}", packageName);
            }

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                logger.debug("Found resource: {}", resource);

                if ("file".equals(resource.getProtocol())) {
                    handleFileProtocol(resource, packageName);
                } else if ("jar".equals(resource.getProtocol())) {
                    handleJarProtocol(resource, packageName);
                } else {
                    logger.warn("Unsupported protocol: {}", resource.getProtocol());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to register request handlers", e);
        }
    }

    private void handleFileProtocol(URL resource, String packageName) throws ReflectiveOperationException, URISyntaxException {
        File dir = new File(resource.toURI());
        logger.debug("Scanning directory: {}", dir.getAbsolutePath());
        scanDirectory(dir, packageName);
    }

    private void scanDirectory(File dir, String packageName) throws ReflectiveOperationException {
        File[] files = dir.listFiles();
        if (files == null) {
            logger.warn("No files found in directory: {}", dir.getAbsolutePath());
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                logger.debug("Processing class: {}", className);
                processClass(className);
            }
        }
    }

    private void handleJarProtocol(URL resource, String packageName) throws IOException, URISyntaxException, ReflectiveOperationException {
        String jarPath = resource.toURI().getSchemeSpecificPart().split("!")[0];
        logger.debug("Scanning JAR file: {}", jarPath);
        try (JarFile jarFile = new JarFile(new File(new URI(jarPath)))) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace("/", ".");
                    if (className.startsWith(packageName)) {
                        logger.debug("Processing class from JAR: {}", className);
                        processClass(className);
                    }
                }
            }
        }
    }

    private void processClass(String className) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(className);
        if (RequestHandler.class.isAssignableFrom(clazz) && !clazz.isInterface() && clazz.isAnnotationPresent(Handler.class)) {
            RequestHandler requestHandler = (RequestHandler) clazz.getDeclaredConstructor().newInstance();
            addRequestHandler(requestHandler);
        }
    }

    public void addRequestHandler(RequestHandler requestHandler) {
        requestHandlers.add(requestHandler);
    }

    public HttpResponse handleRequest(HttpRequest request) {
        return requestHandlers.stream()
                .filter(requestHandler -> requestHandler.canHandle(request))
                .findFirst()
                .map(requestHandler -> requestHandler.handle(request))
                .orElse(staticFileHandler.handle(request));
    }
}
