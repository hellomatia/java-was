package codesquad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    private final Map<String, String> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
        addRoute("/index.html", "/index.html");
        addRoute("/reset.css", "/reset.css");
        addRoute("/global.css", "/global.css");
        addRoute("/main.css", "/main.css");
        addRoute("/img/signiture.svg", "/img/signiture.svg");
        addRoute("/img/like.svg", "/img/like.svg");
        addRoute("/img/sendLink.svg", "/img/sendLink.svg");
        addRoute("/img/bookMark.svg", "/img/bookMark.svg");
        addRoute("/img/ci_chevron-left.svg", "/img/ci_chevron-left.svg");
        addRoute("/img/ci_chevron-right.svg", "/img/ci_chevron-right.svg");
        addRoute("/favicon.ico", "/favicon.ico");
    }

    public void addRoute(String url, String content) {
        routes.put(url, content);
    }

    public String getContent(String url) {
        return routes.getOrDefault(url, "<h1>404 Not Found</h1>");
    }
}
