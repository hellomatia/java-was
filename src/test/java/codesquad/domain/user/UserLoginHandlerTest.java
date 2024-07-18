package codesquad.domain.user;

import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginHandlerTest {
    private UserLoginHandler userLoginHandler;

    @BeforeEach
    void setUp() {
        userLoginHandler = new UserLoginHandler();
    }

    @Test
    void moveLogin_반환_로그인페이지() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/login")
                .build();

        HttpResponse response = userLoginHandler.moveLogin(request);

        assertEquals(200, response.getStatusCode());
        assertTrue(new String(response.getBody()).contains("<html>"));
        assertTrue(new String(response.getBody()).contains("login"));
    }

    @Test
    void processLogin_성공() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .body("userId=admin&password=admin")
                .build();

        HttpResponse response = userLoginHandler.processLogin(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("Location"));

        String setCookieHeader = response.getHeaders().get("Set-Cookie");
        assertNotNull(setCookieHeader);
        assertTrue(setCookieHeader.contains("sid="));
        assertTrue(setCookieHeader.contains("Max-Age=1800"));
        assertTrue(setCookieHeader.contains("HttpOnly"));
    }

    @Test
    void processLogin_실패_잘못된_비밀번호() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .body("userId=admin&password=wrongpassword")
                .build();

        HttpResponse response = userLoginHandler.processLogin(request);

        assertEquals(200, response.getStatusCode());
        assertTrue(new String(response.getBody()).contains("실패"));
    }

    @Test
    void processLogin_실패_존재하지_않는_사용자() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/login")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .body("userId=nonexistent&password=anypassword")
                .build();

        HttpResponse response = userLoginHandler.processLogin(request);

        assertEquals(200, response.getStatusCode());
        assertTrue(new String(response.getBody()).contains("실패"));
    }
}
