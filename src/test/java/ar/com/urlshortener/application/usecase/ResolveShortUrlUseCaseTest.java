package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.domain.exception.ShortUrlExpiredException;
import ar.com.urlshortener.domain.exception.ShortUrlNotFoundException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResolveShortUrlUseCaseTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlClickRepository urlClickRepository;

    @InjectMocks
    private ResolveShortUrlUseCase resolveShortUrlUseCase;

    @Test
    void shouldResolveShortUrl() {
        String code = "abc";
        String originalUrl = "https://example.com";
        ShortCode shortCode = new ShortCode(code);
        ShortUrl shortUrl = ShortUrl.create(shortCode, originalUrl, Instant.now().plusSeconds(3600));

        when(shortUrlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);

        ResolveShortUrlQuery query = new ResolveShortUrlQuery(code, "127.0.0.1", "agent", "referer");
        String result = resolveShortUrlUseCase.execute(query);

        assertEquals(originalUrl, result);
        verify(urlClickRepository).save(any(UrlClick.class));
        verify(shortUrlRepository).save(shortUrl);
        assertEquals(1, shortUrl.getClicks());
    }

    @Test
    void shouldThrowNotFoundWhenNotExists() {
        String code = "none";
        ShortCode shortCode = new ShortCode(code);
        when(shortUrlRepository.findByShortCode(shortCode)).thenReturn(null);

        ResolveShortUrlQuery query = new ResolveShortUrlQuery(code, "127.0.0.1", "agent", "referer");

        assertThrows(ShortUrlNotFoundException.class, () -> resolveShortUrlUseCase.execute(query));
    }

    @Test
    void shouldThrowExpiredWhenIsExpired() {
        String code = "exp";
        ShortCode shortCode = new ShortCode(code);
        ShortUrl shortUrl = ShortUrl.create(shortCode, "https://example.com", Instant.now().minusSeconds(10));

        when(shortUrlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);

        ResolveShortUrlQuery query = new ResolveShortUrlQuery(code, "127.0.0.1", "agent", "referer");

        assertThrows(ShortUrlExpiredException.class, () -> resolveShortUrlUseCase.execute(query));
    }
}
