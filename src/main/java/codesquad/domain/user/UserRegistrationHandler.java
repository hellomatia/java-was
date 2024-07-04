package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class UserRegistrationHandler extends CustomRequestHandler {
    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/registration";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return ok(readFileContent("/static/registration/index.html")).build();
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        if (getMethod().equals(request.getMethod()) && "/register.html".equals(request.getPath())) {
            return true;
        }
        return super.canHandle(request);
    }
}
