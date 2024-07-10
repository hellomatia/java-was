package codesquad.domain.home;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

@Handler("/")
public class HomeHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse handle(HttpRequest request) {
        return ok(readFileContent("/static/index.html")).build();
    }
}
