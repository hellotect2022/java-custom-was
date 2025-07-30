package http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequest {
    private final String path;
    private final String method;
    private final String version;
    private Map<String, String> attributes;
    private final Map<String, String> headers;


    // 본문(raw body)
    private String body; // 나중에 읽는 방식으로
    private final BufferedReader reader;

    public CustomHttpRequest(String rawPath, Map<String, String> headers, String method, String version, BufferedReader reader) {
        this.headers = headers;
        this.method = method;
        this.version = version;
        this.reader = reader;
        String[] parts = rawPath.split("\\?");
        this.path = parts[0];
        if (parts.length > 1) {
            this.attributes = parseQueryParams(parts[1]);  // 메서드 분리
        }
    }

    private Map<String, String> parseQueryParams(String queryString) {
        Map<String, String> map = new HashMap<>();
        for (String pair : queryString.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }

    public String getBody() throws IOException {
        if (body == null && reader != null && headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] chars = new char[contentLength];
            reader.read(chars);
            body = new String(chars);
        }
        return body;
    }

    public Map<String, String> getJsonBodyAsMap() throws IOException {
        if (getHeader("Content-Type").contains("application/json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(getBody(), new TypeReference<>() {});
        }
        return new HashMap<>();
    }

    public String getMethod() {return method;}

    public String getVersion() {return version;}

    public String getPath() {
        return path;
    }

    public String getAtrributes(String name) {
        return attributes.get(name);
    }

    public String getHeader(String key){
        return headers.getOrDefault(key,"");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
