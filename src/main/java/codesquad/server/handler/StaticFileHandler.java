package codesquad.server.handler;

import codesquad.server.http.ContentType;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import static codesquad.server.util.File.loadStaticFiles;
import static codesquad.server.util.File.readFileToByteArray;

public class StaticFileHandler extends AbstractRequestHandler {
    private final Map<String, File> staticFiles;

    public StaticFileHandler() {
        this.staticFiles = loadStaticFiles();
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        File file = staticFiles.get(request.getPath());
        if (file == null || !file.exists() || file.isDirectory()) {
            return notFound().build();
        }

        try {
            byte[] fileContent = readFileToByteArray(file);
            String mimeType = ContentType.getMimeTypeFromFilePath(file.getName());

            return ok(new String(fileContent))
                    .addHeader("Content-Type", mimeType)
                    .addHeader("Content-Length", String.valueOf(fileContent.length))
                    .build();
        } catch (IOException e) {
            return internalServerError().build();
        }
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        return staticFiles.containsKey(request.getPath());
    }
}
