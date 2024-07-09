package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

@Handler("/login")
public class UserLoginHandler extends CustomRequestHandler {
    private final UserRepository userRepository;

    public UserLoginHandler() {
        this.userRepository = UserRepository.INSTANCE;
    }

    @HttpMethod("GET")
    public HttpResponse login(HttpRequest request) {
        return ok(readFileContent("/static/login/index.html")).build();
    }
}
