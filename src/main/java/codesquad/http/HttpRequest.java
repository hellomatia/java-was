package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    private HttpRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.version = builder.version;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
    }

    public String getMethod() { return method; }

    public String getPath() { return path; }

    public String getVersion() { return version; }

    public Map<String, String> getHeaders() { return headers; }

    public String getBody() { return body; }

    public static HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Builder builder = new Builder();

        String requestLine = reader.readLine();
        parseRequestLine(requestLine, builder);
        parseHeaders(reader, builder);
        parseBody(reader, builder);

        return builder.build();
    }

    private static void parseRequestLine(String requestLine, Builder builder) {
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Empty request line");
        }
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }
        builder.method(parts[0])
                .path(parts[1])
                .version(parts[2]);
    }

    private static void parseHeaders(BufferedReader reader, Builder builder) throws IOException {
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

    private static void parseBody(BufferedReader reader, Builder builder) throws IOException {
        if (builder.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(builder.headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars, 0, contentLength);
            builder.body(new String(bodyChars));
        }
    }

    @Override
    public String toString() {
        return String.format("HttpRequest{method='%s', path='%s', version='%s', headers=%s, body='%s'}",
                method, path, version, headers, body);
    }

    public static class Builder {
        private String method;
        private String path;
        private String version;
        private Map<String, String> headers = new HashMap<>();
        private String body;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
