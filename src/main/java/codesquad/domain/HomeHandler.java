package codesquad.domain;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class HomeHandler extends CustomRequestHandler {
    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return ok(readFileContent("/static/index.html")).build();
    }
}
