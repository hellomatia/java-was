package codesquad.server.handler;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public interface RequestHandler {
    HttpResponse handle(HttpRequest request);
    boolean canHandle(HttpRequest request);
}
