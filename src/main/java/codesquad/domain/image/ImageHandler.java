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

import static codesquad.domain.util.Constants.HTTP_METHOD_GET;
import static codesquad.server.util.FileUtils.readFileContent;

@Handler("/image")
public class ImageHandler extends CustomRequestHandler {
    private static final String IMAGE_DIRECTORY = "image";

    @HttpMethod(HTTP_METHOD_GET)
    public HttpResponse getImage(HttpRequest request) {
        String imageName = request.getQueryParam("name");
        byte[] image;

        if (imageName == null) {
            String userId = request.getQueryParam("userId");
            if (userId == null) {
                return noImage();
            }
            User user = DataBase.findUserByUserId(userId);
            if (user == null) {
                return noImage();
            }
            try {
                imageName = user.userImageUrl().split("=")[1];
            } catch (Exception e) {
                return noImage();
            }
        }

        File imageFile = new File(IMAGE_DIRECTORY, imageName);
        if (!imageFile.exists()) {
            return noImage();
        }

        try {
            image = readFileToByteArray(imageFile);
        } catch (IOException e) {
            return noImage();
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

    private HttpResponse noImage() {
        byte[] noImage = readFileContent("/static/img/no_image.png");
        return ok(noImage)
                .addHeader("Content-Type", ContentType.PNG.getMimeType())
                .addHeader("Content-Length", String.valueOf(noImage.length))
                .build();
    }
}
