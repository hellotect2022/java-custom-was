package server;

import config.ConfigLoader;
import config.ServerConfig;
import handler.ErrorPageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerRunner {
    private final Logger logger = LoggerFactory.getLogger(ServerRunner.class);
    private final ServerConfig serverConfig;
    private final ErrorPageHandler errorPageHandler;
    private final Map<String,String> servletMappings;
    private final ExecutorService executorService;

    public ServerRunner() {
        // 1. 설정 로딩
        this.serverConfig = ConfigLoader.getInstance().getServerConfig();
        this.servletMappings = serverConfig.servlet_mappings;
        this.errorPageHandler = new DefaultErrorPageHandler(serverConfig);
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void start() throws IOException {
        int port = serverConfig.port;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[INFO] 서버 시작됨: 포트 "+port);
            while (true) {
                Socket client = serverSocket.accept();
                client.setSoTimeout(3000); // 3초 안에 데이터 안 오면 끊음
                RequestProcessor processor = new RequestProcessor(client, errorPageHandler);
                executorService.execute(processor);
            }
        }catch (IOException ex) {
            logger.error("Error accepting connection", ex);
        }
    }

    public void stop() {
        executorService.shutdown();
    }
}
