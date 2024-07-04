package codesquad.server.handler;

import codesquad.server.http.ContentType;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import java.util.Map;
import static codesquad.server.util.File.loadStaticFiles;

public class StaticFileHandler extends AbstractRequestHandler {
    private final Map<String, byte[]> staticFiles;

    public StaticFileHandler() {
        this.staticFiles = loadStaticFiles();
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        byte[] fileContent = staticFiles.get(request.getPath());
        if (fileContent == null) {
            return notFound().build();
        }

        String mimeType = ContentType.getMimeTypeFromFilePath(request.getPath());

        return ok(fileContent)
                .addHeader("Content-Type", mimeType)
                .addHeader("Content-Length", String.valueOf(fileContent.length))
                .build();
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        return staticFiles.containsKey(request.getPath());
    }
}
