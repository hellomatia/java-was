package codesquad.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class UserJoinHandlerTest {
    private static final String BOUNDARY = "---------------------------1234567890";
    private UserJoinHandler userJoinHandler;

    @BeforeEach
    void 초기화() {
        userJoinHandler = new UserJoinHandler();
    }

    @Test
    void 회원가입_페이지_이동_상태코드_확인() throws IOException {
        HttpRequest request = new HttpRequest.Builder()
                .method("GET")
                .path("/user/create")
                .build();

        HttpResponse response = userJoinHandler.moveJoin(request);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void 회원가입_페이지_이동_본문_확인() throws IOException {
        HttpRequest request = new HttpRequest.Builder()
                .method("GET")
                .path("/user/create")
                .build();

        HttpResponse response = userJoinHandler.moveJoin(request);

        assertTrue(new String(response.getBody()).contains("회원가입"));
    }

    @Test
    void 회원가입_처리_리다이렉트_확인() throws IOException {
        HttpRequest request = createMockMultipartRequest();

        HttpResponse response = userJoinHandler.processJoin(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("Location"));
    }

    private HttpRequest createMockMultipartRequest() throws UnsupportedEncodingException {
        String body = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"name\"\r\n\r\n" +
                "홍길동\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"email\"\r\n\r\n" +
                "hong@example.com\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"password\"\r\n\r\n" +
                "password123\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"userImage\"; filename=\"profile.jpg\"\r\n" +
                "Content-Type: image/jpeg\r\n\r\n" +
                "더미 이미지 데이터\r\n" +
                "--" + BOUNDARY + "--";

        return HttpRequest.builder()
                .method("POST")
                .path("/user/create")
                .addHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
                .bodyBytes(body.getBytes("UTF-8"))
                .build();
    }
}
