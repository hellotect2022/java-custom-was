package http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomHttpResponse {
    private final BufferedWriter writer;
    private int statusCode = 200;
    private Map<String,String> headers = new LinkedHashMap<>();

    public CustomHttpResponse(BufferedWriter writer) {
        this.writer = writer;
        setHeader("Content-Type","text/plain");
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String key, String value) {
        headers.put(key,value);
    }

    public void setContentType(String contentType) {
        setHeader("Content-Type",contentType);
    }

    public void write(String body) throws IOException {
        writer.write("HTTP/1.1 " + statusCode + " " + getStatusMessage(statusCode) + "\r\n");
        for (Map.Entry<String,String> entry : headers.entrySet()) {
            writer.write(entry.getKey()+": "+entry.getValue()+"\r\n");
        }
        writer.write("Content-Length: " + body.getBytes().length + "\r\n");
        writer.write("\r\n");
        writer.write(body);
        writer.flush();
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    private String getStatusMessage(int code) {
        return switch (code) {
            case 200 -> "OK";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
