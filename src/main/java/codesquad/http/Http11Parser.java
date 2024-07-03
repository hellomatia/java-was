package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        builder.method(parts[0])
                .path(parts[1])
                .version(parts[2]);
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
