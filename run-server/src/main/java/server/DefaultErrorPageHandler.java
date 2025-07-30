package server;

import config.ConfigLoader;
import config.HostConfig;
import config.ServerConfig;
import handler.ErrorPageHandler;
import http.CustomHttpRequest;
import http.CustomHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefaultErrorPageHandler implements ErrorPageHandler {
    private final ServerConfig serverConfig;
    public DefaultErrorPageHandler(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle403(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        HostConfig host = getHostConfig(req);
        String filePath = host.http_root + "/" + host.error_pages.get("403");
        String body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        res.setStatus(403);
        res.setHeader("Content-Type", "text/html");
        res.write(body);
    }

    @Override
    public void handle404(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        HostConfig host = getHostConfig(req);
        String filePath = host.http_root + "/" + host.error_pages.get("404");
        String body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        res.setStatus(404);
        res.setHeader("Content-Type", "text/html");
        res.write(body);
    }

    @Override
    public void handle500(CustomHttpRequest req, CustomHttpResponse res) throws IOException {
        HostConfig host = getHostConfig(req);
        String filePath = host.http_root + "/" + host.error_pages.get("500");
        String body;
        try {
            body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            body = "<h1>500 Internal Server Error</h1><pre>" + ex.getMessage() + "</pre>";
        }
        res.setStatus(500);
        res.setHeader("Content-Type", "text/html");
        res.write(body);
    }

    private HostConfig getHostConfig(CustomHttpRequest req) {
        String hostHeader = req.getHeader("Host");
        return serverConfig.hosts.getOrDefault(
                hostHeader,
                serverConfig.hosts.get("default")
        );
    }
}
