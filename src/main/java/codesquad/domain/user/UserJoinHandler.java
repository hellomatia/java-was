package codesquad.domain.user;

import codesquad.domain.user.model.User;
import codesquad.server.Server;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserJoinHandler extends CustomRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final UserRepository userRepository;

    public UserJoinHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/user/create";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String name = request.getQueryParam("name");
        String password = request.getQueryParam("password");
        String userId = request.getQueryParam("userId");
        String email = request.getQueryParam("email");
        User user = new User(name, password, userId, email);
        long id = userRepository.join(user);
        logger.debug("User id: {} joined, User info: {}", id, user);
        return ok(readFileContent("/static/registration/success.html")).build();
    }
}
