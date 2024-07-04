package codesquad.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final String version;
    private final int statusCode;
    private final String statusText;
    private final Map<String, String> headers;
    private final String body;

    private HttpResponse(Builder builder) {
        this.version = builder.version;
        this.statusCode = builder.statusCode;
        this.statusText = builder.statusText;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
    }

    public String getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(version).append(" ")
                .append(statusCode).append(" ")
                .append(statusText).append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey()).append(": ")
                    .append(header.getValue()).append("\r\n");
        }

        response.append("\r\n");
        response.append(body);

        return response.toString();
    }

    public static class Builder {
        private String version = "HTTP/1.1";
        private int statusCode = 200;
        private String statusText = "OK";
        private Map<String, String> headers = new HashMap<>();
        private String body = "";

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusText(String statusText) {
            this.statusText = statusText;
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

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
