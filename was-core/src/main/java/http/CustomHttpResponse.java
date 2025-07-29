package http;

import java.io.BufferedWriter;
import java.io.IOException;

public class CustomHttpResponse {
    private final BufferedWriter writer;
    private int statusCode = 200;
    private String contentType = "text/plain";

    public CustomHttpResponse(BufferedWriter writer) {
        this.writer = writer;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void write(String body) throws IOException {
        writer.write("HTTP/1.1 " + statusCode + " " + getStatusMessage(statusCode) + "\r\n");
        writer.write("Content-Type: " + contentType + "\r\n");
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
