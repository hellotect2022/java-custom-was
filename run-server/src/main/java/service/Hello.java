package service;

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import servlet.CustomBaseServlet;
import servlet.CustomServlet;

import java.io.IOException;

public class Hello extends CustomBaseServlet {
    @Override
    public void doGet(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        res.setStatus(200);
        res.write("Hello!!!");
    }
}
