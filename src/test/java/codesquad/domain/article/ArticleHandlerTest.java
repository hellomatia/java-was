package codesquad.domain.article;

import codesquad.database.DataBase;
import codesquad.domain.article.model.Post;
import codesquad.domain.user.model.User;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArticleHandlerTest {
    private static final String BOUNDARY = "----WebKitFormBoundaryABC123";
    private ArticleHandler articleHandler;

    @BeforeEach
    void setUp() {
        articleHandler = new ArticleHandler();
    }

    @Test
    void moveWriteArticle_인증된_사용자() {
        // 세션 생성 및 사용자 정보 설정
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/article")
                .addCookie("sid", session.getId())
                .build();

        HttpResponse response = articleHandler.moveWriteArticle(request);

        assertEquals(200, response.getStatusCode());
        String responseBody = new String(response.getBody());
        assertTrue(responseBody.contains("<html>"));
        assertTrue(responseBody.contains("작성 완료"));
    }

    @Test
    void moveWriteArticle_인증되지_않은_사용자() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/article")
                .build();

        HttpResponse response = articleHandler.moveWriteArticle(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/login", response.getHeaders().get("Location"));
    }

    @Test
    void writeArticle_인증된_사용자() throws IOException {
        // 세션 생성 및 사용자 정보 설정
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        // Mock MultipartFormDataParser
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "Test Title");
        formData.put("content", "Test Content");

        HttpRequest request = createMockMultipartRequest(session.getId());

        HttpResponse response = articleHandler.writeArticle(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("Location"));

        // 데이터베이스에 게시글이 추가되었는지 확인
        Post addedPost = DataBase.findAllPosts().get(0);
        assertEquals("Test Title", addedPost.title());
        assertEquals("Test Content", addedPost.content());
        assertEquals("admin", addedPost.userId());
        assertEquals("Admin", addedPost.userName());
    }

    @Test
    void writeArticle_인증되지_않은_사용자() throws UnsupportedEncodingException {
        int expect = DataBase.findAllPosts().size();

        HttpRequest request = createMockMultipartRequest("id");

        HttpResponse response = articleHandler.writeArticle(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/login", response.getHeaders().get("Location"));

        // 데이터베이스에 게시글이 추가되지 않았는지 확인
        assertEquals(expect, DataBase.findAllPosts().size());
    }

    private HttpRequest createMockMultipartRequest(String sessionId) throws UnsupportedEncodingException {
        String body = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"title\"\r\n\r\n" +
                "Test Title\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"content\"\r\n\r\n" +
                "Test Content\r\n" +
                "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"image\"; filename=\"test.jpg\"\r\n" +
                "Content-Type: image/jpeg\r\n\r\n" +
                "더미 이미지 데이터\r\n" +
                "--" + BOUNDARY + "--";

        HttpRequest.Builder requestBuilder = HttpRequest.builder()
                .method("POST")
                .path("/article")
                .addHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
                .bodyBytes(body.getBytes("UTF-8"));

        if (sessionId != null) {
            requestBuilder.addCookie("sid", sessionId);
        }

        return requestBuilder.build();
    }
}
