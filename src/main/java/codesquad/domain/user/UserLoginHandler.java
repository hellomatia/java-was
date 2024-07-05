package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class UserLoginHandler extends CustomRequestHandler {
    private final UserRepository userRepository;

    public UserLoginHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
