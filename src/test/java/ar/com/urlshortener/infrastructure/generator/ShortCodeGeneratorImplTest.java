package ar.com.urlshortener.infrastructure.generator;

import ar.com.urlshortener.domain.model.vo.ShortCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortCodeGeneratorImplTest {

    private final ShortCodeGeneratorImpl generator = new ShortCodeGeneratorImpl();

    @Test
    void shouldGenerateCodeOfCorrectLength() {
        ShortCode code = generator.generate();
        assertNotNull(code);
        assertEquals(8, code.value().length());
    }

    @Test
    void shouldGenerateDifferentCodes() {
        ShortCode code1 = generator.generate();
        ShortCode code2 = generator.generate();
        assertNotEquals(code1.value(), code2.value());
    }

    @Test
    void shouldGenerateValidCharacters() {
        ShortCode code = generator.generate();
        assertTrue(code.value().matches("^[0-9a-zA-Z]+$"));
    }
}
