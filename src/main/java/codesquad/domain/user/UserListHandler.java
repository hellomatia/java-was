package codesquad.domain.user;

import codesquad.database.DataBase;
import codesquad.domain.AuthenticatedRequestHandler;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.SessionManager;
import codesquad.server.template.engine.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;

@Handler("/user/list")
public class UserListHandler extends AuthenticatedRequestHandler {
    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse moveUserList(HttpRequest request) throws IOException {
        AuthResult authResult = authenticate(request);
        if (authResult.isAuthenticated) {
            redirectToLogin();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("isLoggedIn", authResult.isAuthenticated);
        data.put("userName", authResult.user.name());
        data.put("users", DataBase.findAllUsers());
        return ok(TemplateEngine.render("user_list", data).getBytes()).build();
    }
}
