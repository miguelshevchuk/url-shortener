package ar.com.urlshortener.infrastructure.config;

import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import ar.com.urlshortener.infrastructure.generator.ShortCodeGeneratorImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class BeanConfig {

    @Bean
    public ShortCodeGenerator shortCodeGenerator(){
        return new ShortCodeGeneratorImpl();
    }

}
