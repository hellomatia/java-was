package codesquad.server.http.parser;

import codesquad.server.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MultipartFormDataParser {
    private static final Logger logger = LoggerFactory.getLogger(MultipartFormDataParser.class);

    private MultipartFormDataParser() {
    }

    public static Map<String, String> parse(HttpRequest request) {
        String contentType = request.getHeaders().get("Content-Type");
        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            throw new IllegalArgumentException("Not a multipart request");
        }

        String[] contentTypeParts = contentType.split(";");
        String boundary = null;
        for (String part : contentTypeParts) {
            if (part.trim().startsWith("boundary=")) {
                boundary = part.substring(part.indexOf('=') + 1).trim();
                break;
            }
        }

        if (boundary == null) {
            logger.error("No boundary found in multipart request");
        }

        return parseMultipartFormData(request.getBody(), boundary);
    }

    private static Map<String, String> parseMultipartFormData(String body, String boundary) {
        Map<String, String> formData = new HashMap<>();
        String[] parts = body.split("--" + boundary);

        for (String part : parts) {
            if (part.contains("Content-Disposition: form-data;")) {
                String[] lines = part.split("\r\n");
                String fieldName = null;
                StringBuilder fieldValue = new StringBuilder();
                boolean isFile = false;

                for (String line : lines) {
                    if (line.contains("Content-Disposition: form-data;")) {
                        fieldName = extractFieldName(line);
                        isFile = line.contains("filename=");
                    } else if (line.trim().isEmpty()) {
                        // 빈 줄 이후부터 실제 내용 시작
                        continue;
                    } else if (!line.startsWith("--") && !line.contains("Content-Type:")) {
                        fieldValue.append(line).append("\n");
                    }
                }

                if (fieldName != null) {
                    if (isFile) {
                        // 파일 데이터 처리
                        formData.put(fieldName, "FILE:" + fieldValue.toString().trim());
                    } else {
                        formData.put(fieldName, fieldValue.toString().trim());
                    }
                }
            }
        }

        return formData;
    }

    private static String extractFieldName(String line) {
        String[] parts = line.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("name=")) {
                return part.substring(6, part.length() - 1);
            }
        }
        return null;
    }
}
