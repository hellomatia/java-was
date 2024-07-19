package codesquad.server.core;

import codesquad.server.handler.RequestHandler;
import codesquad.server.handler.StaticFileHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

import java.util.List;

public class RequestDispatcher {
    private final List<RequestHandler> requestHandlers;
    private final StaticFileHandler staticFileHandler;

    public RequestDispatcher(List<RequestHandler> requestHandlers, StaticFileHandler staticFileHandler) {
        this.requestHandlers = requestHandlers;
        this.staticFileHandler = staticFileHandler;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        return requestHandlers.stream()
                .filter(handler -> handler.canHandle(request))
                .findFirst()
                .map(handler -> handler.handle(request))
                .orElseGet(() -> staticFileHandler.handle(request));
    }
}
