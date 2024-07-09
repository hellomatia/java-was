package codesquad.domain.user;

import codesquad.database.UserRepository;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

@Handler("/login")
public class UserLoginHandler extends CustomRequestHandler {

    public UserLoginHandler() {
    }

    @HttpMethod("GET")
    public HttpResponse login(HttpRequest request) {
        return ok(readFileContent("/static/login/index.html")).build();
    }
}
