package ar.com.urlshortener.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración de la aplicación mapeadas desde application.properties/yml.
 */
@ConfigurationProperties(prefix = "app.config")
@Data
public class AppProperties {

    /**
     * URL base que se prefijará a los códigos cortos generados.
     */
    private String baseUrl;

    /**
     * Tiempo de expiración por defecto para las URLs cortas, en segundos.
     */
    private int expirationSeconds;

}
