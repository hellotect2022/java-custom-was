package servlet;

import java.io.IOException;
import java.io.Writer;

public abstract class CustomBaseServlet implements CustomServlet{
    protected void writeOk(Writer writer, String contentType, String body) throws IOException {
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: "+contentType+"\r\n\r\n");
        writer.write("Content-Length: " + body.length() + "\r\n");
        writer.write(body);
        writer.flush();
    }

    protected void writeError(Writer writer, int statusCode, String message) throws IOException {
        writer.write("HTTP/1.1 "+statusCode+" Error\r\n");//out.write("Content-Type: text/plain\r\n");
        writer.write("Content-Type: text/html\r\n");
        writer.write("Content-Length: " + message.length() + "\r\n");
        writer.write("\r\n");
        writer.write(message);
        writer.flush();
    }
}
