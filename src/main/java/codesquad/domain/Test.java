package codesquad.domain;

import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import codesquad.server.template.engine.TemplateEngine;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Handler("/test")
public class Test extends CustomRequestHandler {
    @HttpMethod("GET")
    public HttpResponse test(HttpRequest request) throws IOException {
        TemplateEngine engine = new TemplateEngine();

        Map<String, Object> data = new HashMap<>();
        data.put("title", "My Web Page");
        data.put("showContent", true);
        data.put("items", Arrays.asList("Apple", "Banana", "Cherry"));

        String renderedHtml = engine.render("src/main/resources/templates/template.html", data);
        System.out.println(renderedHtml);
        return ok(renderedHtml.getBytes())
                .build();
    }
}
