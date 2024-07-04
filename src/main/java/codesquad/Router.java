package codesquad;

import codesquad.handler.RequestHandler;
import codesquad.handler.StaticFileHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Router instance;
    private final List<RequestHandler> requestHandlers;
    private final StaticFileHandler staticFileHandler;

    private Router() {
        requestHandlers = new CopyOnWriteArrayList<>();
        staticFileHandler = new StaticFileHandler();
    }

    public static synchronized Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
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
