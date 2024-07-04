package codesquad.app;

import codesquad.domain.HomeHandler;
import codesquad.domain.article.ArticleHandler;
import codesquad.domain.user.UserJoinHandler;
import codesquad.domain.user.UserLoginHandler;
import codesquad.domain.user.UserRegistrationHandler;
import codesquad.domain.user.UserRepository;
import codesquad.server.Server;

import java.io.IOException;

public class Application {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Server server = new Server(PORT);
        server.addRequestHandler(new HomeHandler());
        server.addRequestHandler(new UserLoginHandler());
        server.addRequestHandler(new UserRegistrationHandler());
        server.addRequestHandler(new UserJoinHandler(new UserRepository()));
        server.addRequestHandler(new ArticleHandler());
        server.start();
    }
}
