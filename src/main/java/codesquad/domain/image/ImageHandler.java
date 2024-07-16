package codesquad.domain.image;

import codesquad.database.DataBase;
import codesquad.domain.user.model.User;
import codesquad.server.handler.CustomRequestHandler;
import codesquad.server.handler.annotation.Handler;
import codesquad.server.handler.annotation.HttpMethod;
import codesquad.server.http.ContentType;
import codesquad.server.http.HttpRequest;
import codesquad.server.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Handler("/image")
public class ImageHandler extends CustomRequestHandler {
    private static final String IMAGE_DIRECTORY = "image";

    @HttpMethod("GET")
    public HttpResponse getImage(HttpRequest request) {
        String imageName = request.getQueryParam("name");
        byte[] image;

        if (imageName == null) {
            String userId = request.getQueryParam("userId");
            if (userId == null) {
                return notFound().build();
            }
            User user = DataBase.findUserByUserId(userId);
            if (user == null) {
                return notFound().build();
            }
            imageName = user.userImageUrl().split("=")[1];
        }

        File imageFile = new File(IMAGE_DIRECTORY, imageName);
        if (!imageFile.exists()) {
            return notFound().build();
        }

        try {
            image = readFileToByteArray(imageFile);
        } catch (IOException e) {
            return internalServerError().build();
        }

        String mimeType = ContentType.getMimeTypeFromFilePath(imageName);
        return ok(image)
                .addHeader("Content-Type", mimeType)
                .addHeader("Content-Length", String.valueOf(image.length))
                .build();
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
}
