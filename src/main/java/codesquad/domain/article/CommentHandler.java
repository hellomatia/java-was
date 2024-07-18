package codesquad.domain.article;

import codesquad.database.DataBase;
import codesquad.domain.article.model.Comment;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.UrlEncodedBodyParser;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;

import java.util.Map;

@Handler("/comment")
public class CommentHandler extends CustomRequestHandler {
    @HttpMethod("POST")
    public HttpResponse handle(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        String postId = request.getQueryParams().getOrDefault("postId", "-1");
        Map<String, String> data = UrlEncodedBodyParser.parse(request.getBody());
        if (sessionId != null && SessionManager.getSession(sessionId) != null && !postId.equals("-1")) {
            Session session = SessionManager.getSession(sessionId);
            User user = (User) session.getAttribute("userInfo");
            Comment comment = new Comment(
                    null,
                    postId,
                    user.userId(),
                    user.name(),
                    data.get("comment"),
                    null,
                    null
                    );
            DataBase.addComment(comment);
            return redirect("/").build();
        }
        return redirect("/login").build();
    }
}
