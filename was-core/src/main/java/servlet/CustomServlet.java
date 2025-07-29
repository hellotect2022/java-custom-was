package servlet;

import http.CustomHttpRequest;
import http.CustomHttpResponse;

import java.io.IOException;

public interface CustomServlet {
    void service(CustomHttpRequest req, CustomHttpResponse res) throws IOException;
}
