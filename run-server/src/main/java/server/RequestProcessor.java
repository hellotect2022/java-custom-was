package server;

import handler.ErrorPageHandler;
import http.CustomHttpRequest;
import http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CustomServlet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
            logger.info("Request-Line: "+requestLine);
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fullPath = tokens[1];
            String version = tokens[2];

            Map<String,String> requestHeaders = processHeaders(in);
            CustomHttpRequest req = new CustomHttpRequest(fullPath, requestHeaders);
            CustomHttpResponse res = new CustomHttpResponse(out);

            String className = req.getPath().substring(1);

            try {
                // Java Reflection API 사용
                // JVM 이 현재 classpath 에서 해당 이름의 클래스를 동적으로 찾아서 Class 객체로 반환
                //  Class<?> 는 class 의 메타데이타
                Class<?> clazz = Class.forName(className);
                // clazz.getDeclaredConstructor() : 클래스의 **기본 생성자(파라미터 없는 생성자)**를 가져옴
                // .newInstance : 생성자를 실행해서 실제 객체를 생성
                Object obj = clazz.getDeclaredConstructor().newInstance();

                if (obj instanceof CustomServlet) {
                    ((CustomServlet) obj).service(req,res);
                    res.getWriter().flush();
                } else {
                    throw new ClassCastException(className + "is not a CustomServlet");
                }
            }catch (ClassNotFoundException e) {
                errorPageHandler.handle404(req,res);

            }catch (Exception e) {
                errorPageHandler.handle500(req,res);
                logger.error(e.getMessage(),e);
            }

            conn.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public Map<String,String> processHeaders(BufferedReader in) throws IOException {
        Map<String,String> requestHeaders = new HashMap<>();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            logger.info("Request: "+ line);
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
