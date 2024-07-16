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

import java.io.IOException;

import static codesquad.server.util.FileUtils.readFileContent;
import static codesquad.server.util.FileUtils.saveImage;

@Handler("/article")
public class ArticleHandler extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse article(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            return ok(readFileContent("/static/article/index.html")).build();
        }
        return redirect("/login").build();
    }

    @HttpMethod("POST")
    public HttpResponse writeArticle(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        MultipartFormDataParser.ParsedData parsedData = MultipartFormDataParser.parse(request);
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            Session session = SessionManager.getSession(sessionId);
            User user = (User) session.getAttribute("userInfo");

            String imageUrl = null;
            if (!parsedData.getFileData().isEmpty()) {
                try {
                    MultipartFormDataParser.FileData imageFile = parsedData.getFileData().get("image");
                    if (imageFile != null) {
                        imageUrl = saveImage(imageFile);
                    }
                } catch (IOException e) {
                    return internalServerError().build();
                }
            }

            Post post = new Post(null,
                    parsedData.getFormData().get("title"),
                    user.userId(),
                    user.name(),
                    imageUrl,
                    parsedData.getFormData().get("content"),
                    null,
                    null,
                    null);

            DataBase.addPost(post);
            return redirect("/").build();
        }
        return redirect("/login").build();
    }
}
