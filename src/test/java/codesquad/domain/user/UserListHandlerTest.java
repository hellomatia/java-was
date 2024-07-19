package codesquad.domain.user;

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

class UserListHandlerTest {
    private UserListHandler userListHandler;

    @BeforeEach
    void setUp() {
        userListHandler = new UserListHandler();
    }

    @Test
    void moveUserList_인증된_사용자() throws IOException {
        // 세션 생성 및 사용자 정보 설정
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/user/list")
                .addCookie("sid", session.getId())
                .build();

        HttpResponse response = userListHandler.moveUserList(request);

        assertEquals(200, response.getStatusCode());
        String responseBody = new String(response.getBody());
        assertTrue(responseBody.contains("Admin"));
    }

    @Test
    void moveUserList_인증되지_않은_사용자() throws IOException {
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/user/list")
                .build();

        HttpResponse response = userListHandler.moveUserList(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/login", response.getHeaders().get("Location"));
    }

    @Test
    void moveUserList_세션_만료() throws IOException {
        // 만료된 세션 ID 사용
        HttpRequest request = HttpRequest.builder()
                .method("GET")
                .path("/user/list")
                .addCookie("sid", "expired_session_id")
                .build();

        HttpResponse response = userListHandler.moveUserList(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/login", response.getHeaders().get("Location"));
    }
}
