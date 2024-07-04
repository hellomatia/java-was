package codesquad;

import codesquad.handler.StaticFileHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Router instance;
    private final StaticFileHandler staticFileHandler;

    private Router() {
        staticFileHandler = new StaticFileHandler();
    }

    public static synchronized Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        return staticFileHandler.handle(request);
    }
}
