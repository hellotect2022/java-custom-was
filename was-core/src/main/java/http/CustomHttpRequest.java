package http;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequest {
    private final String path;
    private final String method;
    private final String version;
    private final Map<String, String> attributes;
    private final Map<String, String> headers;

    public CustomHttpRequest(String rawPath, Map<String, String> headers, String method, String version) {
        this.headers = headers;
        this.method = method;
        this.version = version;

        String[] parts = rawPath.split("\\?");
        this.path = parts[0];
        this.attributes = new HashMap<>();
        if (parts.length > 1) {
            for (String pair : parts[1].split("&")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) attributes.put(kv[0],kv[1]);
            }
        }
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
