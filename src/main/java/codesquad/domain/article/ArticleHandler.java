package codesquad.domain.article;

import codesquad.database.DataBase;
import codesquad.domain.article.model.Post;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.MultipartFormDataParser;
import codesquad.server.session.Session;
import codesquad.server.session.SessionManager;

import java.util.Map;

import static codesquad.server.util.FileUtils.readFileContent;

@Handler("/article")
public class ArticleHandler extends CustomRequestHandler {

    @HttpMethod("GET")
    public HttpResponse article(HttpRequest request) {
        return ok(readFileContent("/static/article/index.html")).build();
    }

    @HttpMethod("POST")
    public HttpResponse writeArticle(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        Map<String, String> formData = MultipartFormDataParser.parse(request);
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            Session session = SessionManager.getSession(sessionId);
            User user = (User) session.getAttribute("userInfo");
            Post post = new Post(null,
                    formData.get("title"),
                    user.userId(),
                    user.name(),
                    "",
                    formData.get("content"),
                    null,
                    null);

            DataBase.addPost(post);
            return redirect("/").build();
        }
        return redirect("/login").build();
    }
}
