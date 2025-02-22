package codesquad.domain.user;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.SessionManager;

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;

@Handler("/logout")
public class UserLogoutHandler extends CustomRequestHandler {
    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse processLogout(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId != null) {
            SessionManager.invalidateSession(sessionId);
        }
        return redirect("/")
                .addCookie("sid", "", 0, true)
                .build();
    }
}
