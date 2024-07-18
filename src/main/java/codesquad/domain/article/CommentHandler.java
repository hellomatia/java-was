package codesquad.domain.article;

import codesquad.database.DataBase;
import codesquad.domain.AuthenticatedRequestHandler;
import codesquad.domain.article.model.Comment;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.http.parser.UrlEncodedBodyParser;

import java.util.Map;

import static codesquad.domain.util.Constants.HTTP_METHOD_POST;

@Handler("/comment")
public class CommentHandler extends AuthenticatedRequestHandler {
    @HttpMethod(HTTP_METHOD_POST)
    public HttpResponse writeComment(HttpRequest request) {
        String postId = request.getQueryParams().getOrDefault("postId", "-1");
        Map<String, String> data = UrlEncodedBodyParser.parse(request.getBody());
        AuthResult authResult = authenticate(request);
        if (!authResult.isAuthenticated) {
            redirectToLogin();
        }
        Comment comment = new Comment(
                null,
                postId,
                authResult.user.userId(),
                authResult.user.name(),
                data.get("comment")
        );
        DataBase.addComment(comment);
        return redirect("/").build();
    }
}
