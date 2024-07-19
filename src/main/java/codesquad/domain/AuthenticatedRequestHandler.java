package codesquad.domain;

import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;

public abstract class AuthenticatedRequestHandler extends CustomRequestHandler {
    protected AuthResult authenticate(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            Session session = SessionManager.getSession(sessionId);
            User user = (User) session.getAttribute("userInfo");
            return new AuthResult(true, user, session);
        }
        return new AuthResult(false, null, null);
    }

    protected static class AuthResult {
        public final boolean isAuthenticated;
        public final User user;
        public final Session session;

        public AuthResult(boolean isAuthenticated, User user, Session session) {
            this.isAuthenticated = isAuthenticated;
            this.user = user;
            this.session = session;
        }
    }

    protected HttpResponse redirectToLogin() {
        return redirect("/login").build();
    }
}
