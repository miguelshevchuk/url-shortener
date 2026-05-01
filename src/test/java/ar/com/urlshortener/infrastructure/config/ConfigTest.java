package ar.com.urlshortener.infrastructure.config;

import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void shouldCreateShortCodeGeneratorBean() {
        BeanConfig beanConfig = new BeanConfig();
        ShortCodeGenerator generator = beanConfig.shortCodeGenerator();
        assertNotNull(generator);
    }

    @Test
    void appPropertiesTest() {
        AppProperties properties = new AppProperties();
        properties.setBaseUrl("http://localhost");
        properties.setExpirationSeconds(3600);
        
        assertEquals("http://localhost", properties.getBaseUrl());
        assertEquals(3600, properties.getExpirationSeconds());
    }
}
