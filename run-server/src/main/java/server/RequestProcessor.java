package server;

import config.ConfigLoader;
import config.HostConfig;
import handler.ErrorPageHandler;
import http.CustomHttpRequest;
import http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CustomServlet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private final Socket conn;
    private final ErrorPageHandler errorPageHandler;


    public RequestProcessor(Socket conn, ErrorPageHandler errorPageHandler) {
        this.conn = conn;
        this.errorPageHandler = errorPageHandler;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));) {


            // 요청 읽기
            String requestLine = in.readLine();
            if (requestLine == null) return;
            logger.info("Request-Line: "+requestLine);
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fullPath = tokens[1];
            String version = tokens[2];

            Map<String,String> requestHeaders = processHeaders(in);
            CustomHttpRequest req = new CustomHttpRequest(fullPath, requestHeaders, method, version, in);
            CustomHttpResponse res = new CustomHttpResponse(out);


            try {
                String path = req.getPath();
                String className;
                Map<String, String> mappings = ConfigLoader.getInstance().getServerConfig().servlet_mappings;

                // 0. /../../ 가 포함되어있거나
                // .exe 요청시 forbidden
                if (isForbiddenAccess(req)) {
                    throw new AccessDeniedException("권한 없음");
                }

                // 1. 루트 요청이면 index.html 반환 시도
                if ("/".equals(path)) {
                    String host = req.getHeaders().getOrDefault("Host", "default");
                    HostConfig hostConfig = ConfigLoader.getInstance()
                            .getServerConfig()
                            .hosts
                            .getOrDefault(host, ConfigLoader.getInstance().getServerConfig().hosts.get("default"));

                    String indexPath = hostConfig.http_root + "/index.html";

                    try {
                        String body = new String(Files.readAllBytes(Paths.get(indexPath)), StandardCharsets.UTF_8);
                        res.setStatus(200);
                        res.setContentType("text/html");
                        res.write(body);
                    } catch (IOException e) {
                        errorPageHandler.handle404(req, res);  // or handle500
                    }
                }


                if (mappings.containsKey(path)) {
                    className = mappings.get(path);
                } else {
                    // fallback 로직: "/Hello" → "Hello"
                    className = path.startsWith("/") ? path.substring(1) : path;
                }
                // Java Reflection API 사용
                // JVM 이 현재 classpath 에서 해당 이름의 클래스를 동적으로 찾아서 Class 객체로 반환
                //  Class<?> 는 class 의 메타데이타
                Class<?> clazz = Class.forName(className);
                // clazz.getDeclaredConstructor() : 클래스의 **기본 생성자(파라미터 없는 생성자)**를 가져옴
                // .newInstance : 생성자를 실행해서 실제 객체를 생성
                Object obj = clazz.getDeclaredConstructor().newInstance();

                if (obj instanceof CustomServlet) {
                    ((CustomServlet) obj).service(req,res);
                } else {
                    throw new ClassCastException(className + "is not a CustomServlet");
                }
            }catch (ClassNotFoundException e) {
                errorPageHandler.handle404(req,res);

            }catch (AccessDeniedException e){
                errorPageHandler.handle403(req,res);
            } catch (Exception e) {
                errorPageHandler.handle500(req,res);
                logger.error(e.getMessage(),e);
            }

            conn.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isForbiddenAccess(CustomHttpRequest req) {
        String path = req.getPath();

        // 조건 1: 디렉터리 탈출 시도
        if (path.contains("..")) {
            return true;
        }

        // 조건 2: .exe 파일 요청 차단
        if (path.toLowerCase().endsWith(".exe")) {
            return true;
        }

        return false;
    }

    public Map<String,String> processHeaders(BufferedReader in) throws IOException {
        Map<String,String> requestHeaders = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            logger.debug("Request: "+ line);
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0,colonIndex).trim();
                String value = line.substring(colonIndex+1).trim();
                requestHeaders.put(key,value);
            }
        }
        return requestHeaders;
    }

}
