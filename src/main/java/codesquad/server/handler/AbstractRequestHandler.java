package codesquad.server.handler;

import codesquad.server.http.ContentType;
import codesquad.server.http.HttpResponse;
import codesquad.server.util.File;
import java.io.InputStream;
import static codesquad.server.handler.Constants.ERROR_PAGE_PATH;
import static codesquad.server.util.File.readInputStreamToByteArray;

public abstract class AbstractRequestHandler implements RequestHandler {
    protected HttpResponse.Builder ok(byte[] body) {
        return HttpResponse.builder()
                .statusCode(200)
                .statusText("OK")
                .body(body);
    }

    protected HttpResponse.Builder notFound() {
        byte[] errorContent = readErrorPage(404);
        return HttpResponse.builder()
                .statusCode(404)
                .statusText("Not Found")
                .addHeader("Content-Type", ContentType.HTML.getMimeType())
                .body(errorContent);
    }

    protected HttpResponse.Builder internalServerError() {
        byte[] errorContent = readErrorPage(500);
        return HttpResponse.builder()
                .statusCode(500)
                .statusText("Internal Server Error")
                .addHeader("Content-Type", ContentType.HTML.getMimeType())
                .body(errorContent);
    }

    private byte[] readErrorPage(int errorCode) {
        String errorPagePath = ERROR_PAGE_PATH + errorCode + "." + ContentType.HTML.getExtension();
        try {
            return readFileContent(errorPagePath);
        } catch (NullPointerException e) {
            return "<html><body><h1>404 Not Found</h1></body></html>".getBytes();
        }
    }

    protected byte[] readFileContent(String filePath) {
        try {
            InputStream resourceUrl = File.class.getResourceAsStream(filePath);
            return readInputStreamToByteArray(resourceUrl);
        } catch (Exception e) {
            return readErrorPage(500);
        }
    }
}
