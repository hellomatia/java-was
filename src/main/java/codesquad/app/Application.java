package codesquad.app;

import codesquad.domain.user.UserJoinHandler;
import codesquad.domain.user.UserRepository;
import codesquad.server.Server;

import java.io.IOException;

public class Application {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Server server = new Server(PORT);
        server.addRequestHandler(new UserJoinHandler(new UserRepository()));
        server.start();
    }
}
