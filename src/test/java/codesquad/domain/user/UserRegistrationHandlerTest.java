package codesquad.domain.user;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationHandlerTest {
    private UserRegistrationHandler userRegistrationHandler;

    @BeforeEach
    void 초기화() {
        userRegistrationHandler = new UserRegistrationHandler();
    }

    @Test
    void 요청_처리_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/registration")
                .build();

        HttpResponse response = userRegistrationHandler.handle(request);

        assertNotNull(response);
        assertTrue(new String(response.getBody()).contains("<html>"));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void 처리_가능_여부_확인_정상_경로() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/registration")
                .build();

        assertTrue(userRegistrationHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_메서드() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/registration")
                .build();

        assertFalse(userRegistrationHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_경로() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/wrong-path")
                .build();

        assertFalse(userRegistrationHandler.canHandle(request));
    }
}
