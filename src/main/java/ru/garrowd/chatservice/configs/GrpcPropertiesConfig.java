package ru.garrowd.chatservice.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.garrowd.chatservice.utils.Client;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "grpc")
@Data
public class GrpcPropertiesConfig {
    private Map<String, Client> client;
}
