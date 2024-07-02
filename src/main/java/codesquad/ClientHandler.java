package codesquad;

import codesquad.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private static final String STATIC_PATH = "src/main/resources/static";
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
            HttpRequest request = HttpRequest.parse(in);
            logger.debug("Client received: " + request);
            String content = router.getContent(request.getPath());
            if (content.startsWith("/")) {
                sendFile(out, content);
            } else {
                sendResponse(out, "text/html", content);
            }
        } catch (IOException e) {
            logger.error("Error handling client request", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    private void sendFile(OutputStream out, String filePath) throws IOException {
        File file = new File(STATIC_PATH + filePath);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(filePath);
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write(("Content-Type: " + contentType + "\r\n").getBytes());
            out.write("\r\n".getBytes());

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            out.flush();
        }
    }

    private void sendResponse(OutputStream out, String contentType, String response) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(response.getBytes());
        out.flush();
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html")) { return "text/html"; }
        if (filePath.endsWith(".css")) { return "text/css"; }
        if (filePath.endsWith(".js")) { return "application/javascript"; }
        if (filePath.endsWith(".ico")) { return "image/x-icon"; }
        if (filePath.endsWith(".png")) { return "image/png"; }
        if (filePath.endsWith(".jpeg")) { return "image/jpeg"; }
        if (filePath.endsWith(".svg")) { return "image/svg+xml"; }
        return "text/plain";
    }
}
