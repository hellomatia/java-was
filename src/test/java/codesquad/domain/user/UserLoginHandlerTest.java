package codesquad.domain.user;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginHandlerTest {
    private UserLoginHandler userLoginHandler;
    private UserRepository userRepository;

    @BeforeEach
    void 초기화() {
        userRepository = new UserRepository();
        userLoginHandler = new UserLoginHandler(userRepository);
    }

    @Test
    void GET_메서드_확인() {
        assertEquals("GET", userLoginHandler.getMethod());
    }

    @Test
    void 경로_확인() {
        assertEquals("/login", userLoginHandler.getPath());
    }

    @Test
    void 요청_처리_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/login")
                .build();

        HttpResponse response = userLoginHandler.handle(request);

        assertNotNull(response);
        assertTrue(new String(response.getBody()).contains("<html>"));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void 처리_가능_여부_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/login")
                .build();

        assertTrue(userLoginHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_메서드() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .build();

        assertFalse(userLoginHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_경로() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/wrong-path")
                .build();

        assertFalse(userLoginHandler.canHandle(request));
    }
}
