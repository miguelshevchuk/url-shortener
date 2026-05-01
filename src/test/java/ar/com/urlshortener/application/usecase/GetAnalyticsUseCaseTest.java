package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.dto.AnalitycsResult;
import ar.com.urlshortener.application.query.GetAnalitycsQuery;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.IpAddress;
import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.model.vo.UserAgent;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAnalyticsUseCaseTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlClickRepository urlClickRepository;

    @InjectMocks
    private GetAnalyticsUseCase getAnalyticsUseCase;

    @Test
    void shouldGetAnalytics() {
        String code = "abc";
        ShortCode shortCode = new ShortCode(code);
        ShortUrl shortUrl = new ShortUrl(1L, shortCode, new OriginalUrl("https://example.com"), Instant.now(), null, 5L);
        UrlClick click = new UrlClick(1L, shortCode, Instant.now(), new UserAgent("Agent"), new IpAddress("127.0.0.1"), "Ref");

        when(shortUrlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);
        when(urlClickRepository.findByShortUrl(shortCode)).thenReturn(List.of(click));

        AnalitycsResult result = getAnalyticsUseCase.execute(new GetAnalitycsQuery(code));

        assertNotNull(result);
        assertEquals(code, result.shortCode());
        assertEquals(5L, result.clicks());
        assertEquals(1, result.clicksDetails().size());
        verify(shortUrlRepository).findByShortCode(shortCode);
        verify(urlClickRepository).findByShortUrl(shortCode);
    }
}
