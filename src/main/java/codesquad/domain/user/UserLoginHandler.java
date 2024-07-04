package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class UserLoginHandler extends CustomRequestHandler {
    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/login";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return ok(readFileContent("/static/login/index.html")).build();
    }
}
