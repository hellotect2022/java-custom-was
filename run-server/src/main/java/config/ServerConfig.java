package config;

import java.util.Map;


public class ServerConfig {
    public int port =8080;
    public Map<String, HostConfig> hosts;
    public Map<String,String> servlet_mappings;
}
