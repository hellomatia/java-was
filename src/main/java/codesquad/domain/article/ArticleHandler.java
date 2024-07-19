package codesquad.domain.article;

import codesquad.database.DataBase;
import codesquad.domain.AuthenticatedRequestHandler;
import codesquad.domain.article.model.Post;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.MultipartFormDataParser;

import java.io.IOException;

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;
import static codesquad.domain.util.Constants.HTTP_METHOD_POST;
import static codesquad.server.util.FileUtils.readFileContent;
import static codesquad.server.util.FileUtils.saveImage;

@Handler("/article")
public class ArticleHandler extends AuthenticatedRequestHandler {
    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse moveWriteArticle(HttpRequest request) {
        AuthResult authResult = authenticate(request);
        if (authResult.isAuthenticated) {
            return ok(readFileContent("/static/article/index.html")).build();
        }
        return redirectToLogin();
    }

    @HttpMethod(HTTP_METHOD_POST)
    public HttpResponse writeArticle(HttpRequest request) {
        MultipartFormDataParser.ParsedData parsedData = MultipartFormDataParser.parse(request);
        AuthResult authResult = authenticate(request);
        if (!authResult.isAuthenticated) {
            return redirectToLogin();
        }
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
                authResult.user.userId(),
                authResult.user.name(),
                imageUrl,
                parsedData.getFormData().get("content"),
                null);

        DataBase.addPost(post);
        return redirect("/").build();
    }
}
