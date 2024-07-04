package codesquad;

import codesquad.http.Http11Parser;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final Router router;

    public ClientHandler(Socket socket, Router router) {
        this.clientSocket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream()
        ) {
            logger.debug("Client connected");
            HttpRequest request = Http11Parser.parse(in);
            logger.debug("Client received: " + request);
            HttpResponse response = router.handleRequest(request);
            sendResponse(out, response);
        } catch (Exception e) {
            logger.error("Error handling client request", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    private static void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.toString().getBytes());
        out.flush();
    }
}
