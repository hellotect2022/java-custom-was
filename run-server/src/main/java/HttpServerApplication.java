import com.fasterxml.jackson.databind.ObjectMapper;

import config.ConfigLoader;
import config.ServerConfig;
import handler.ErrorPageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.DefaultErrorPageHandler;
import server.RequestProcessor;
import server.ServerRunner;

import java.io.*;


public class HttpServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerApplication.class);


    public static void main(String[] args) throws Exception {
        logger.debug("DHhan Boot init!!");

        try {
            ServerRunner runner = new ServerRunner();
            runner.start();
        } catch (IOException ex) {
            logger.error("Server could not start", ex);
        }
    }
}
