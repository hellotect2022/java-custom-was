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
import java.nio.charset.StandardCharsets;


public class HttpServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerApplication.class);


    public static void main(String[] args) throws Exception {
        logger.debug("DHhan Boot init!!");
        printBannerFromResource();

        try {
            ServerRunner runner = new ServerRunner();
            runner.start();
        } catch (IOException ex) {
            logger.error("Server could not start", ex);
        }
    }

    private static void printBannerFromResource() {
        try (InputStream is = HttpServerApplication.class.getClassLoader().getResourceAsStream("banner.txt")) {
            if (is != null) {
                String banner = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(banner);
            } else {
                System.out.println(":: DHHAN BOOT ::");
            }
        } catch (IOException e) {
            System.out.println(":: DHHAN BOOT :: (banner load failed)");
            e.printStackTrace();
        }
    }
}
