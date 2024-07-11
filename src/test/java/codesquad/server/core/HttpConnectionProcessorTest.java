package codesquad.server.core;

import codesquad.server.handler.StaticFileHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

class HttpConnectionProcessorTest {
    private TestSocket testSocket;
    private RequestDispatcher requestDispatcher;
    private HttpConnectionProcessor httpConnectionProcessor;

    @BeforeEach
    void setUp() {
        testSocket = new TestSocket();
        requestDispatcher = new RequestDispatcher(new HandlerScanner("codesquad.domain").scanForHandlers(), new StaticFileHandler());
        httpConnectionProcessor = new HttpConnectionProcessor(testSocket, requestDispatcher);
    }

    @Test
    void 유효한_요청_처리() throws IOException, NoSuchAlgorithmException {
        // Arrange
        String request = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        testSocket.setInputStream(new ByteArrayInputStream(request.getBytes()));

        // 실제 파일 정보 가져오기
        File file = new File("src/main/resources/static/index.html");
        long fileSize = file.length();
        String fileHash = getFileHash(file);

        // Act
        httpConnectionProcessor.run();

        // Assert
        String response = testSocket.getOutputString();

        // 1. 헤더 정보 확인
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));
        assertTrue(response.contains("Content-Length: " + fileSize));

        // 2. 파일 크기 확인
        assertEquals(fileSize, getContentLength(response));

        // 3. 해시값 비교
        assertEquals(fileHash, getResponseBodyHash(response));

        // 4. 파일의 시작 부분 확인 (예: 처음 100바이트)
        String fileStart = getFileStart(file, 100);
        assertTrue(response.contains(fileStart));
    }

    private long getContentLength(String response) {
        String[] lines = response.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("Content-Length: ")) {
                return Long.parseLong(line.substring("Content-Length: ".length()));
            }
        }
        return -1;
    }

    private String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private String getResponseBodyHash(String response) throws NoSuchAlgorithmException {
        int bodyStart = response.indexOf("\r\n\r\n") + 4;
        String body = response.substring(bodyStart);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(body.getBytes());
        return bytesToHex(digest);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private String getFileStart(File file, int bytes) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[bytes];
            int read = is.read(buffer);
            return new String(buffer, 0, read);
        }
    }

    // 테스트용 Socket 클래스
    private static class TestSocket extends Socket {
        private InputStream inputStream;
        private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        void setInputStream(InputStream is) {
            this.inputStream = is;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }

        String getOutputString() {
            return outputStream.toString();
        }
    }
}
