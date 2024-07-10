package codesquad.domain.home;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeHandlerTest {
    private HomeHandler homeHandler;

    @BeforeEach
    void 초기화() {
        homeHandler = new HomeHandler();
    }

    @Test
    void 요청_처리_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .build();
        HttpResponse response = homeHandler.handle(request);

        assertNotNull(response);
        assertTrue(new String(response.getBody()).contains("<html>"));
        assertEquals(200, response.getStatusCode());
    }
}
