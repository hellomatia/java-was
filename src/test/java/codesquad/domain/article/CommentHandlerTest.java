package codesquad.domain.article;

import static org.junit.jupiter.api.Assertions.*;

import codesquad.database.DataBase;
import codesquad.domain.article.model.Comment;
import codesquad.domain.user.model.User;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommentHandlerTest {
    private CommentHandler commentHandler;

    @BeforeEach
    void setUp() {
        commentHandler = new CommentHandler();
    }

    @Test
    void writeComment_인증되지_않은_사용자() {
        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/comment")
                .addQueryParam("postId", "100")
                .body("comment=This comment should not be added")
                .build();

        HttpResponse response = commentHandler.writeComment(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/login", response.getHeaders().get("Location"));

        List<Comment> comments = DataBase.findCommentsByPostId("100");
        assertTrue(comments.isEmpty());
    }

    @Test
    void writeComment_인증된_사용자() {
        // 세션 생성 및 사용자 정보 설정
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/comment")
                .addQueryParam("postId", "1")
                .addCookie("sid", session.getId())
                .body("comment=This is a test comment")
                .build();

        HttpResponse response = commentHandler.writeComment(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("Location"));

        List<Comment> comments = DataBase.findCommentsByPostId("1");
        assertFalse(comments.isEmpty());
        Comment addedComment = comments.get(0);
        assertEquals("1", addedComment.postId());
        assertEquals("admin", addedComment.userId());
        assertEquals("Admin", addedComment.userName());
        assertEquals("This is a test comment", addedComment.comment());
    }

    @Test
    void writeComment_잘못된_postId() {
        // 세션 생성 및 사용자 정보 설정
        Session session = SessionManager.createSession();
        User user = DataBase.findUserByUserId("admin");
        session.setAttribute("userInfo", user);

        HttpRequest request = HttpRequest.builder()
                .method("POST")
                .path("/comment")
                .addCookie("sid", session.getId())
                .body("comment=This is a test comment")
                .build();

        HttpResponse response = commentHandler.writeComment(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("Location"));

        List<Comment> comments = DataBase.findCommentsByPostId("-1");
        assertFalse(comments.isEmpty());
        Comment addedComment = comments.get(0);
        assertEquals("-1", addedComment.postId());
    }
}
