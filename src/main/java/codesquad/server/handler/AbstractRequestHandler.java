package codesquad.server.handler;

import codesquad.server.http.ContentType;
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
                .addHeader("Content-Type", ContentType.HTML.getMimeType())
                .body(errorContent);
    }

    protected HttpResponse.Builder internalServerError() {
        String errorContent = readErrorPage(500);
        return HttpResponse.builder()
                .statusCode(500)
                .statusText("Internal Server Error")
                .addHeader("Content-Type", ContentType.HTML.getMimeType())
                .body(errorContent);
    }

    private String readErrorPage(int errorCode) {
        String errorPagePath = ERROR_PAGE_PATH + errorCode + "." + ContentType.HTML.getExtension();
        try {
            return readFileContent(errorPagePath);
        } catch (NullPointerException e) {
            return "<html><body><h1>404 Not Found</h1></body></html>";
        }
    }

    protected String readFileContent(String filePath) {
        try {
            File file = new File(getClass().getResource(filePath).getFile());
            if (!file.exists() || file.isDirectory()) {
                return null;
            }
            byte[] fileContent = readFileToByteArray(file);
            return new String(fileContent, "UTF-8");
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }
}
