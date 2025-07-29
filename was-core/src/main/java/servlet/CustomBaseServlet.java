package servlet;

import http.CustomHttpRequest;
import http.CustomHttpResponse;

import java.io.IOException;
import java.io.Writer;

public abstract class CustomBaseServlet implements CustomServlet{
    @Override
    public void service(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        String method = req.getMethod();
        switch (method) {
            case "GET": doGet(req, res); break;
            case "POST": doPost(req, res); break;
            case "PUT": doPut(req, res); break;
            case "DELETE": doDelete(req, res); break;
            default:
                res.setStatus(405);
                res.write("405 Method Not Allowed");
        }
    }

    protected void doGet(CustomHttpRequest req, CustomHttpResponse res) throws IOException {}
    protected void doPost(CustomHttpRequest req, CustomHttpResponse res) throws IOException {}
    protected void doPut(CustomHttpRequest req, CustomHttpResponse res) throws IOException {}
    protected void doDelete(CustomHttpRequest req, CustomHttpResponse res) throws IOException {}
}
