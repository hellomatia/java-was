package codesquad.http;

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

        public String getHeader(String name) {
            return this.headers.get(name);
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
