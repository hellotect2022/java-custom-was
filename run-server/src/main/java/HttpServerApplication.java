import com.fasterxml.jackson.databind.ObjectMapper;

import config.ConfigLoader;
import config.ServerConfig;
import handler.ErrorPageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.DefaultErrorPageHandler;
import server.RequestProcessor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerApplication.class);

    private final int SERVER_PORT;

    public HttpServerApplication() throws Exception {
        this.SERVER_PORT = ConfigLoader.getInstance().getServerConfig().port;
    }

    public void start() throws IOException {
        ServerConfig config = ConfigLoader.getInstance().getServerConfig();

        ErrorPageHandler errorHandler = new DefaultErrorPageHandler(config);
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            logger.info("Accepting connections on port " + server.getLocalPort());
            try {
                while (true) {
                    Socket client = server.accept();
                    pool.submit(new RequestProcessor(client, errorHandler));
                }
            }catch (IOException ex) {
                logger.error("Error accepting connection", ex);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        logger.debug("DHhan Boot init!!");

        try {
            HttpServerApplication webserver = new HttpServerApplication();
            webserver.start();
        } catch (IOException ex) {
            logger.error("Server could not start", ex);
        }
    }
}
