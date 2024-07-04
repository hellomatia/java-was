package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface RequestHandler {
    HttpResponse handle(HttpRequest request);
    boolean canHandle(HttpRequest request);
}
