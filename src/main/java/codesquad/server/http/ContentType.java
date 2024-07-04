package codesquad.server.http;

import java.util.Arrays;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static String getMimeType(String filePath) {
        String extension = getExtension(filePath);
        return Arrays.stream(values())
                .filter(ct -> ct.extension.equalsIgnoreCase(extension))
                .map(ct -> ct.mimeType)
                .findFirst()
                .orElse("text/plain");
    }

    private static String getExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return (dotIndex > 0) ? filePath.substring(dotIndex + 1) : "";
    }
}
