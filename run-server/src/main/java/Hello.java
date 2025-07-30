

import http.CustomHttpRequest;
import http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CustomBaseServlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class Hello extends CustomBaseServlet {
    private final Logger logger = LoggerFactory.getLogger(Hello.class);

    @Override
    public void doGet(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        res.setStatus(200);
        res.write("Hello!!!");
    }

    @Override
    public void doPost(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        Map<String,String> body = req.getJsonBodyAsMap();
        logger.info("doPost : ",body);

        res.setStatus(200);
        res.write("Hello!!!"+ req.getJsonBodyAsMap().toString());
    }
}
