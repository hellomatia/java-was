package codesquad.handler;

import codesquad.http.HttpRequest;

public abstract class CustomRequestHandler extends AbstractRequestHandler {
    abstract String getMethod();

    abstract String getPath();

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getMethod().equalsIgnoreCase(getMethod()) && request.getPath().equals(getPath());
    }
}
