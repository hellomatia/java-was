package codesquad.domain.user;

import codesquad.database.DataBase;
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

@Handler("/user/list")
public class UserListHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse shoUserList(HttpRequest request) throws IOException {
        String sessionId = request.getCookie("sid");
        if (sessionId == null || SessionManager.getSession(sessionId) == null) {
            ;
            return redirect("/login").build();
        }
        Map<String, Object> data = new HashMap<>();
        User user = (User) SessionManager
                .getSession(sessionId)
                .getAttribute("userInfo");
        data.put("isLoggedIn", true);
        data.put("userName", user.name());
        data.put("users", DataBase.findAllUsers());
        return ok(TemplateEngine.render("user_list", data).getBytes()).build();
    }
}
