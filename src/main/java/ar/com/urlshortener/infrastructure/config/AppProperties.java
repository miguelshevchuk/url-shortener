package ar.com.urlshortener.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.config")
@Data
public class AppProperties {

    private String baseUrl;
    private int expirationSeconds;

}
