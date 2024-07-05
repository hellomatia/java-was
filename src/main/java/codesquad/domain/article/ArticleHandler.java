package codesquad.domain.article;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

public class ArticleHandler extends CustomRequestHandler {
    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/article";
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return ok(readFileContent("/static/article/index.html")).build();
    }
}
