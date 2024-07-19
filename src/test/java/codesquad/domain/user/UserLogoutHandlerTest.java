package codesquad.domain.user;

import codesquad.server.session.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.SessionManager;

import static org.junit.jupiter.api.Assertions.*;

class UserLogoutHandlerTest {
    private UserLogoutHandler userLogoutHandler;
    private Session session;

    @BeforeEach
    void setUp() {
        userLogoutHandler = new UserLogoutHandler();
        session = SessionManager.createSession();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 세션 정리
        SessionManager.invalidateSession(session.getId());
    }

    @Test
    void 로그아웃_성공_세션있음() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/logout")
                .addCookie("sid", session.getId())
                .build();

        HttpResponse response = userLogoutHandler.processLogout(request);

        assertEquals(302, response.getStatusCode(), "Status code should be 302 (redirect)");
        assertEquals("/", response.getHeaders().get("Location"), "Redirect location should be '/'");

        String setCookieHeader = response.getHeaders().get("Set-Cookie");
        assertNotNull(setCookieHeader, "Set-Cookie header should be present");
        assertTrue(setCookieHeader.contains("sid="), "Cookie 'sid' should be present");
        assertTrue(setCookieHeader.contains("Max-Age=0"), "Cookie should have Max-Age=0");
        assertTrue(setCookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");

        assertNull(SessionManager.getSession(session.getId()), "Session should be invalidated");
    }

    @Test
    void 로그아웃_성공_세션없음() {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/logout")
                .build();

        HttpResponse response = userLogoutHandler.processLogout(request);

        assertEquals(302, response.getStatusCode(), "Status code should be 302 (redirect)");
        assertEquals("/", response.getHeaders().get("Location"), "Redirect location should be '/'");

        String setCookieHeader = response.getHeaders().get("Set-Cookie");
        assertNotNull(setCookieHeader, "Set-Cookie header should be present");
        assertTrue(setCookieHeader.contains("sid="), "Cookie 'sid' should be present");
        assertTrue(setCookieHeader.contains("Max-Age=0"), "Cookie should have Max-Age=0");
        assertTrue(setCookieHeader.contains("HttpOnly"), "Cookie should be HttpOnly");
    }
}
