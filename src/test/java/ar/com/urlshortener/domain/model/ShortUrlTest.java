package ar.com.urlshortener.domain.model;

import ar.com.urlshortener.domain.model.vo.ShortCode;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlTest {

    @Test
    void shouldCreateShortUrl() {
        ShortCode shortCode = new ShortCode("test");
        String originalUrl = "https://example.com";
        Instant expiresAt = Instant.now().plusSeconds(3600);

        ShortUrl shortUrl = ShortUrl.create(shortCode, originalUrl, expiresAt);

        assertNotNull(shortUrl);
        assertEquals(shortCode, shortUrl.getShortCode());
        assertEquals(originalUrl, shortUrl.getOriginalUrl().value());
        assertEquals(expiresAt, shortUrl.getExpiresAt());
        assertEquals(0, shortUrl.getClicks());
        assertNotNull(shortUrl.getCreateAt());
    }

    @Test
    void shouldBeExpired() {
        ShortUrl shortUrl = ShortUrl.create(new ShortCode("test"), "https://example.com", Instant.now().minusSeconds(10));
        assertTrue(shortUrl.isExpired(Instant.now()));
    }

    @Test
    void shouldNotBeExpired() {
        ShortUrl shortUrl = ShortUrl.create(new ShortCode("test"), "https://example.com", Instant.now().plusSeconds(10));
        assertFalse(shortUrl.isExpired(Instant.now()));
    }

    @Test
    void shouldNotBeExpiredWhenExpiresAtIsNull() {
        ShortUrl shortUrl = new ShortUrl(1L, new ShortCode("test"), null, Instant.now(), null, 0);
        assertFalse(shortUrl.isExpired(Instant.now()));
    }

    @Test
    void shouldRegisterClick() {
        ShortUrl shortUrl = ShortUrl.create(new ShortCode("test"), "https://example.com", Instant.now().plusSeconds(10));
        shortUrl.registerClick();
        assertEquals(1, shortUrl.getClicks());
    }

    @Test
    void testSettersAndGetters() {
        ShortUrl shortUrl = new ShortUrl(null, null, null, null, null, 0);
        shortUrl.setId(1L);
        shortUrl.setClicks(10L);
        assertEquals(1L, shortUrl.getId());
        assertEquals(10L, shortUrl.getClicks());
    }
}
