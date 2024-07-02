package codesquad.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {
    @Test
    void 정상적인_GET_요청_파싱() throws IOException {
        String requestString =
                "GET /index.html HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "User-Agent: Mozilla/5.0\r\n" +
                        "\r\n";

        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = HttpRequest.parse(inputStream);

        assertEquals("GET", request.getMethod());
        assertEquals("/index.html", request.getPath());
        assertEquals("HTTP/1.1", request.getVersion());
        assertEquals("localhost:8080", request.getHeaders().get("Host"));
        assertEquals("Mozilla/5.0", request.getHeaders().get("User-Agent"));
        assertNull(request.getBody());
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    void 다양한_HTTP_메소드_테스트(String method) throws IOException {
        String requestString =
                method + " /api/resource HTTP/1.1\r\n" +
                        "Host: api.example.com\r\n" +
                        "\r\n";

        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = HttpRequest.parse(inputStream);

        assertEquals(method, request.getMethod());
        assertEquals("/api/resource", request.getPath());
    }

    @Test
    void 본문이_있는_POST_요청_파싱() throws IOException {
        String requestString =
                "POST /submit HTTP/1.1\r\n" +
                        "Host: example.com\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: 18\r\n" +
                        "\r\n" +
                        "{\"key\":\"value\"}";

        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = HttpRequest.parse(inputStream);

        assertEquals("POST", request.getMethod());
        assertEquals("/submit", request.getPath());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("18", request.getHeaders().get("Content-Length"));
        assertEquals("{\"key\":\"value\"}", request.getBody());
    }

    @Test
    void 빈_요청_처리() {
        String emptyRequestString = "";
        InputStream inputStream = new ByteArrayInputStream(emptyRequestString.getBytes());

        assertThrows(IllegalArgumentException.class, () -> HttpRequest.parse(inputStream));
    }

    @Test
    void 한글_데이터_처리() throws IOException {
        String koreanContent = "안녕하세요";
        String requestString =
                "POST /submit HTTP/1.1\r\n" +
                        "Host: example.com\r\n" +
                        "Content-Type: text/plain; charset=UTF-8\r\n" +
                        "Content-Length: " + koreanContent.getBytes("UTF-8").length + "\r\n" +
                        "\r\n" +
                        koreanContent;

        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes("UTF-8"));
        HttpRequest request = HttpRequest.parse(inputStream);

        assertEquals(koreanContent, request.getBody());
    }

    @Test
    void 긴_헤더_값_처리() throws IOException {
        String longValue = "a".repeat(1000);
        String requestString =
                "GET /long-header HTTP/1.1\r\n" +
                        "X-Long-Header: " + longValue + "\r\n" +
                        "\r\n";

        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
        HttpRequest request = HttpRequest.parse(inputStream);

        assertEquals(longValue, request.getHeaders().get("X-Long-Header"));
    }
}
