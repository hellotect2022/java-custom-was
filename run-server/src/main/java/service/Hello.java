package service;

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import servlet.CustomServlet;

import java.io.IOException;

public class Hello implements CustomServlet {
    @Override
    public void service(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        res.setStatus(200);
        res.write("Hello!!!");
    }
}
