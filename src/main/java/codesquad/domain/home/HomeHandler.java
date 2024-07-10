package codesquad.domain.home;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.SessionManager;

@Handler("/")
public class HomeHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse showMainPage(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            return ok(readFileContent("/static/main/index.html")).build();
        } else {
            return ok(readFileContent("/static/index.html")).build();
        }
    }
}
