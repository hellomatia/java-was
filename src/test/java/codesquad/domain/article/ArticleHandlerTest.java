package codesquad.domain.article;

import codesquad.domain.user.UserJoinHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleHandlerTest {
    private ArticleHandler articleHandler;

    @BeforeEach
    void 초기화() {
        articleHandler = new ArticleHandler();
    }

    @Test
    void 요청_처리_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/article")
                .build();

        HttpResponse response = articleHandler.handle(request);

        assertNotNull(response);
        assertTrue(new String(response.getBody()).contains("<html>"));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void 처리_가능_여부_확인() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/article")
                .build();

        assertTrue(articleHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_메서드() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/article")
                .build();

        assertFalse(articleHandler.canHandle(request));
    }

    @Test
    void 처리_불가능_여부_확인_잘못된_경로() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/wrong-path")
                .build();

        assertFalse(articleHandler.canHandle(request));
    }
}