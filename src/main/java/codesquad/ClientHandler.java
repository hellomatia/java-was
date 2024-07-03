package codesquad;

import codesquad.http.ContentType;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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

    private static HttpResponse createFileResponse(String filePath) throws IOException {
        File file = new File(filePath);
        logger.debug("Processing file request for: {}", filePath);

        if (!file.exists() || file.isDirectory()) {
            return createNotFoundResponse();
        }

        String contentType = getMimeType(filePath);
        byte[] fileContent = readFileToByteArray(file);

        return HttpResponse.builder()
                .statusCode(getStatusCode(filePath))
                .statusText(getStatusText(filePath))
                .addHeader("Content-Type", contentType)
                .addHeader("Content-Length", String.valueOf(fileContent.length))
                .body(new String(fileContent))
                .build();
    }

    private static int getStatusCode(String filePath) {
        return filePath.endsWith("404.html") ? 404 : 200;
    }

    private static String getStatusText(String filePath) {
        return filePath.endsWith("404.html") ? "Not Found" : "OK";
    }

    private static HttpResponse createNotFoundResponse() throws IOException {
        File notFoundFile = new File("404.html");
        if (notFoundFile.exists() && !notFoundFile.isDirectory()) {
            return createFileResponse(notFoundFile.getPath());
        }

        return HttpResponse.builder()
                .statusCode(404)
                .statusText("Not Found")
                .addHeader("Content-Type", "text/plain")
                .body("404 File Not Found")
                .build();
    }

    private static byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    private static void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.toString().getBytes());
        out.flush();
    }

    private static String getMimeType(String filePath) {
        return ContentType.getMimeType(filePath);
    }
}
