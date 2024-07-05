package codesquad.server;

import codesquad.server.handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int THREAD_POOL_SIZE = 10; // 스레드 풀 크기 설정
    private final int port;
    private final RequestDispatcher requestDispatcher;

    public Server(int port) {
        this.port = port;
        this.requestDispatcher = RequestDispatcher.getInstance();
    }

    public void addRequestHandler(RequestHandler handler) {
        requestDispatcher.addRequestHandler(handler);
    }

    public void start() throws IOException {
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.debug("Listening for connection on port {} ....", port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new HttpConnectionProcessor(clientSocket, requestDispatcher));
            }
        } finally {
            executor.shutdown();
        }
    }
}
