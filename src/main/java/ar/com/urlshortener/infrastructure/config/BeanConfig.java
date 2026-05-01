package ar.com.urlshortener.infrastructure.config;

import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import ar.com.urlshortener.infrastructure.generator.ShortCodeGeneratorImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para la instanciación de beans de la capa de infraestructura.
 */
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class BeanConfig {

    /**
     * Define el bean para la generación de códigos cortos.
     *
     * @return Una instancia de ShortCodeGeneratorImpl.
     */
    @Bean
    public ShortCodeGenerator shortCodeGenerator(){
        return new ShortCodeGeneratorImpl();
    }

}
