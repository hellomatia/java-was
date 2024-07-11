package codesquad.app;

import codesquad.server.core.Server;
import java.io.IOException;

public class Application {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException {
        Server server = new Server(PORT, THREAD_POOL_SIZE, "codesquad.domain");
        server.start();
    }
}
