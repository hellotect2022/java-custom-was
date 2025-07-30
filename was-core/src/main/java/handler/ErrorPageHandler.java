package handler;

import http.CustomHttpRequest;
import http.CustomHttpResponse;

import java.io.IOException;

public interface ErrorPageHandler {
    void handle403(CustomHttpRequest req, CustomHttpResponse res) throws IOException;
    void handle404(CustomHttpRequest req, CustomHttpResponse res) throws IOException;
    void handle500(CustomHttpRequest req, CustomHttpResponse res) throws IOException;
}
