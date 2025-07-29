package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private final ServerConfig serverConfig;


    private ConfigLoader() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 리소스 내에서 파일 읽기
            InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("config.json");
            if (is == null) throw new RuntimeException("config.json 파일을 찾을 수가 없습니다. !!");
            serverConfig = objectMapper.readValue(is, ServerConfig.class);
        }catch (Exception e) {
            logger.error("Config 로딩 실패", e);
            throw new RuntimeException("Config 로딩 실패", e);
        }
    }

    public static ConfigLoader getInstance() {
        return LazyHolder.holder;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    private static class LazyHolder {
        private static final ConfigLoader holder = new ConfigLoader();
    }
}
