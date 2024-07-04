package codesquad.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Http11Parser {
    private Http11Parser() {
    }

    public static HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        HttpRequest.Builder builder = HttpRequest.builder();

        String requestLine = reader.readLine();
        parseRequestLine(requestLine, builder);
        parseHeaders(reader, builder);
        parseBody(reader, builder);

        return builder.build();
    }

    private static void parseRequestLine(String requestLine, HttpRequest.Builder builder) {
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Empty request line");
        }
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }
        builder.method(parts[0]);
        parsePathAndQueryParams(parts[1], builder);
        builder.version(parts[2]);
    }

    private static void parseHeaders(BufferedReader reader, HttpRequest.Builder builder) throws IOException {
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            int colonIndex = headerLine.indexOf(':');
            if (colonIndex > 0) {
                String name = headerLine.substring(0, colonIndex).trim();
                String value = headerLine.substring(colonIndex + 1).trim();
                builder.addHeader(name, value);
            }
        }
    }

    private static void parsePathAndQueryParams(String fullPath, HttpRequest.Builder builder) {
        String decodedPath = urlDecode(fullPath);
        String[] pathParts = decodedPath.split("\\?", 2);
        builder.path(pathParts[0]);

        if (pathParts.length > 1) {
            Map<String, String> queryParams = parseQueryParams(pathParts[1]);
            builder.queryParams(queryParams);
        } else {
            builder.queryParams(new HashMap<>());
        }
    }

    private static Map<String, String> parseQueryParams(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = urlDecode(pair.substring(0, idx));
                String value = idx < pair.length() - 1 ? urlDecode(pair.substring(idx + 1)) : "";
                queryParams.put(key, value);
            } else {
                queryParams.put(urlDecode(pair), "");
            }
        }
        return queryParams;
    }

    private static String urlDecode(String value) {
        if (value == null) {
            return null;
        }
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (IllegalArgumentException e) {
            return value.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        }
    }

    private static void parseBody(BufferedReader reader, HttpRequest.Builder builder) throws IOException {
        String contentLengthHeader = builder.getHeader("Content-Length");
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            char[] bodyChars = new char[contentLength];
            int charsRead = reader.read(bodyChars, 0, contentLength);
            if (charsRead > 0) {
                builder.body(new String(bodyChars, 0, charsRead).trim());
            }
        }
    }
}
