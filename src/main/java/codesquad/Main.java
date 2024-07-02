package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10; // 스레드 풀 크기 설정

    public static void main(String[] args) throws IOException {
        Router router = new Router();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.debug("Listening for connection on port {} ....", PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket, router));
            }
        } finally {
            executor.shutdown();
        }
    }
}
