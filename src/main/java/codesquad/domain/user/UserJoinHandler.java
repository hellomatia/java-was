package codesquad.domain.user;

import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class UserJoinHandler extends CustomRequestHandler {
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
        return ok("회원가입 성공!! id: " + id).build();
    }
}
