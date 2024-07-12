package codesquad.domain.article;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

import static codesquad.server.util.FileUtils.readFileContent;

@Handler("/article")
public class ArticleHandler extends CustomRequestHandler {

    @HttpMethod("GET")
    public HttpResponse article(HttpRequest request) {
        return ok(readFileContent("/static/article/index.html")).build();
    }
}
