package codesquad.server.handler;

import codesquad.server.http.HttpRequest;

public abstract class CustomRequestHandler extends AbstractRequestHandler {
    protected abstract String getMethod();

    protected abstract String getPath();

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getMethod().equalsIgnoreCase(getMethod()) && request.getPath().equals(getPath());
    }
}
