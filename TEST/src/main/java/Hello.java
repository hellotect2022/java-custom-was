

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import servlet.CustomBaseServlet;

import java.io.IOException;
import java.io.Writer;

public class Hello extends CustomBaseServlet {

    @Override
    public void service(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        Writer writer = res.getWriter();
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/plain\r\n\r\n");
        writer.write("Hello, ");
    }
}
