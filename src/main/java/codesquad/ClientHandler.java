package codesquad;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
            HttpRequest request = HttpRequest.parse(in);
            logger.debug("Client received: " + request);
            String filePath = router.getFilePath(request.getPath());
            HttpResponse response = createFileResponse(filePath);
            sendResponse(out, response);
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

    private HttpResponse createFileResponse(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            return HttpResponse.builder()
                    .statusCode(200)
                    .statusText("OK")
                    .addHeader("Content-Type", contentType)
                    .addHeader("Content-Length", String.valueOf(fileContent.length))
                    .body(new String(fileContent)) // 주의: 텍스트 파일 가정. 바이너리 파일의 경우 다르게 처리해야 함
                    .build();
        }
        return HttpResponse.builder()
                .statusCode(404)
                .statusText("Not Found")
                .addHeader("Content-Type", "text/plain")
                .body("404 File Not Found")
                .build();
    }

    private void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.toString().getBytes());
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
