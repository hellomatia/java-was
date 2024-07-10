package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import static codesquad.server.util.FileUtils.readFileContent;

@Handler("/registration")
public class UserRegistrationHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse registration(HttpRequest request) {
        return ok(readFileContent("/static/registration/index.html"))
                .build();
    }
}
