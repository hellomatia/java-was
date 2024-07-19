package codesquad.domain.home;

import codesquad.database.DataBase;
import codesquad.domain.user.model.User;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HomeHandlerTest {
    private HomeHandler homeHandler;

    @BeforeEach
    void setUp() {
        homeHandler = new HomeHandler();
    }

    @Test
    void moveHome_인증된_사용자() throws IOException {
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .addCookie("sid", session.getId())
                .build();

        HttpResponse response = homeHandler.moveHome(request);

        assertEquals(200, response.getStatusCode());
        String responseBody = new String(response.getBody());
        assertTrue(responseBody.contains("Admin"));
    }

    @Test
    void moveHome_인증되지_않은_사용자() throws IOException {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/")
                .build();

        HttpResponse response = homeHandler.moveHome(request);

        assertEquals(200, response.getStatusCode());
        String responseBody = new String(response.getBody());
        assertFalse(responseBody.contains("회원가입"));
    }
}
