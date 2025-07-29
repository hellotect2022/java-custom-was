

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import servlet.CustomBaseServlet;

import java.io.IOException;
import java.io.Writer;

public class Hello extends CustomBaseServlet {

    @Override
    public void service(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        res.setStatus(200);
        res.write("Hello!!!");
    }
}
