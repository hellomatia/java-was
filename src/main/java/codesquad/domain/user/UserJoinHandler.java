package codesquad.domain.user;

import codesquad.domain.user.model.User;
import codesquad.server.Server;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.UrlEncodedBodyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserJoinHandler extends CustomRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final UserRepository userRepository;

    public UserJoinHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected String getMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
        return "/user/create";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Map<String, String> params = UrlEncodedBodyParser.parse(request.getBody());
        String name = params.get("name");
        String email = params.get("email");
        String password = params.get("password");
        String userId = params.get("userId");
        User user = new User(name, password, userId, email);
        long id = userRepository.join(user);
        logger.debug("User id: {} joined, User info: {}", id, user);
        return redirect("/index.html").build();
    }
}
