package server;

import config.ConfigLoader;
import config.HostConfig;
import config.ServerConfig;
import handler.ErrorPageHandler;
import http.CustomHttpRequest;
import http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefaultErrorPageHandler implements ErrorPageHandler {
    private final Logger logger = LoggerFactory.getLogger(DefaultErrorPageHandler.class);
    private final ServerConfig serverConfig;
    public DefaultErrorPageHandler(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle403(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        try {
            HostConfig host = getHostConfig(req);
            String filePath = host.http_root + "/" + host.error_pages.get("403");
            String body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            res.setStatus(403);
            res.setHeader("Content-Type", "text/html");
            res.write(body);
        }catch (IOException e){
            logger.warn("403 응답 전송 실패 (클라이언트 연결 끊김 가능성): {}", e.getMessage());
        }
    }

    @Override
    public void handle404(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        try {
            HostConfig host = getHostConfig(req);
            String filePath = host.http_root + "/" + host.error_pages.get("404");
            String body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            res.setStatus(404);
            res.setHeader("Content-Type", "text/html");
            res.write(body);
        }catch (IOException e) {
            logger.warn("404 응답 전송 실패 (클라이언트 연결 끊김 가능성): {}", e.getMessage());
        }

    }

    @Override
    public void handle500(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        try {
            HostConfig host = getHostConfig(req);
            String filePath = host.http_root + "/" + host.error_pages.get("500");
            String body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            res.setStatus(500);
            res.setHeader("Content-Type", "text/html");
            res.write(body);
        } catch (IOException e){
            logger.warn("500 응답 전송 실패 (클라이언트 연결 끊김 가능성): {}", e.getMessage());
        }
    }

    private HostConfig getHostConfig(CustomHttpRequest req) {
        String hostHeader = req.getHeader("Host");
        return serverConfig.hosts.getOrDefault(
                hostHeader,
                serverConfig.hosts.get("default")
        );
    }
}
