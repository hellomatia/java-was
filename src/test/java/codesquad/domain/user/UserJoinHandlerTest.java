package codesquad.domain.user;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserJoinHandlerTest {
    private UserJoinHandler userJoinHandler;

    @BeforeEach
    void 초기화() {
        userJoinHandler = new UserJoinHandler();
    }

    @Test
    void 요청_처리_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/user/create")
                .body("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net")
                .build();

        HttpResponse response = userJoinHandler.handle(request);

        assertNotNull(response);
        assertEquals(302, response.getStatusCode());
    }

    @Test
    void 처리_가능_여부_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/user/create")
                .build();

        assertTrue(userJoinHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_메서드() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/user/create")
                .build();

        assertFalse(userJoinHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_경로() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/wrong-path")
                .build();

        assertFalse(userJoinHandler.canHandle(request));
    }
}
