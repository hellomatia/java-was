package codesquad.server.handler;

import codesquad.server.http.HttpResponse;
import java.io.File;
import java.io.IOException;
import static codesquad.server.handler.Constants.ERROR_PAGE_PATH;
import static codesquad.server.util.File.readFileToByteArray;

public abstract class AbstractRequestHandler implements RequestHandler {
    protected HttpResponse.Builder ok(String body) {
        return HttpResponse.builder()
                .statusCode(200)
                .statusText("OK")
                .body(body);
    }

    protected HttpResponse.Builder notFound() {
        String errorContent = readErrorPage(404);
        return HttpResponse.builder()
                .statusCode(404)
                .statusText("Not Found")
                .addHeader("Content-Type", "text/html")
                .body(errorContent);
    }

    protected HttpResponse.Builder internalServerError() {
        String errorContent = readErrorPage(500);
        return HttpResponse.builder()
                .statusCode(500)
                .statusText("Internal Server Error")
                .addHeader("Content-Type", "text/html")
                .body(errorContent);
    }

    private String readErrorPage(int errorCode) {
        String errorPagePath = ERROR_PAGE_PATH + errorCode + ".html";
        try {
            File errorFile = new File(getClass().getResource(errorPagePath).getFile());
            return new String(readFileToByteArray(errorFile));
        } catch (IOException | NullPointerException e) {
            return "<html><body><h1>404 Not Found</h1></body></html>";
        }
    }
}
