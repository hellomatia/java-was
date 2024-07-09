package codesquad.domain.user;

import codesquad.database.DataBase;
import codesquad.database.UserRepository;
import codesquad.domain.user.model.User;
import codesquad.server.Server;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.UrlEncodedBodyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Handler("/user/create")
public class UserJoinHandler extends CustomRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public UserJoinHandler() {
    }

    @HttpMethod("POST")
    public HttpResponse createUser(HttpRequest request) {
        Map<String, String> params = UrlEncodedBodyParser.parse(request.getBody());
        String name = params.get("name");
        String email = params.get("email");
        String password = params.get("password");
        String userId = params.get("userId");
        User user = new User(name, password, userId, email);
        long id = DataBase.addUser(user);
        logger.debug("User id: {} joined, User info: {}", id, user);
        return redirect("/index.html").build();
    }
}
