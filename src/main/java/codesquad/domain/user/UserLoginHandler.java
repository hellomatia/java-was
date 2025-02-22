package codesquad.domain.user;

import codesquad.database.DataBase;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.UrlEncodedBodyParser;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;

import java.util.Map;
import java.util.Objects;

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;
import static codesquad.domain.util.Constants.HTTP_METHOD_POST;
import static codesquad.server.util.FileUtils.readFileContent;

@Handler("/login")
public class UserLoginHandler extends CustomRequestHandler {
    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse moveLogin(HttpRequest request) {
        return ok(readFileContent("/static/login/index.html")).build();
    }

    @HttpMethod(HTTP_METHOD_POST)
    public HttpResponse processLogin(HttpRequest request) {
        Map<String, String> params = UrlEncodedBodyParser.parse(request.getBody());
        String userId = params.get("userId");
        String password = params.get("password");

        User user = DataBase.findUserByUserId(userId);
        if (user == null || !Objects.equals(user.password(), password)) {
            return ok(readFileContent("/static/login/login_failed.html")).build();
        }
        Session session = SessionManager.createSession();
        session.setAttribute("userInfo", user);
        return redirect("/")
                .addCookie("sid", session.getId(), 30 * 60, true)
                .build();
    }
}
