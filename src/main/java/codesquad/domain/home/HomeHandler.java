package codesquad.domain.home;

import codesquad.database.DataBase;
import codesquad.domain.article.model.Comment;
import codesquad.domain.article.model.Post;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;
import codesquad.server.template.engine.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Handler("/")
public class HomeHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse showMainPage(HttpRequest request) throws IOException {
        String sessionId = request.getCookie("sid");
        Map<String, Object> data = new HashMap<>();
        List<Post> posts = DataBase.findAllPosts();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            List<Comment> comments = DataBase.findCommentsByPostId(post.id());
            posts.set(i, post.addComments(comments));
        }
        data.put("posts", posts);
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            Session session = SessionManager.getSession(sessionId);
            User user = (User) session.getAttribute("userInfo");
            data.put("isLoggedIn", true);
            data.put("userName", user.name());
            return ok(TemplateEngine.render("main", data).getBytes()).build();
        } else {
            data.put("isLoggedIn", false);
            data.put("userName", "");
            return ok(TemplateEngine.render("main", data).getBytes()).build();
        }
    }
}
