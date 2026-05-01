package ar.com.urlshortener.infrastructure.rest.mapper;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.AnalitycsResult;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.application.dto.UrlClickDTO;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlRequest;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlResponse;
import ar.com.urlshortener.infrastructure.rest.model.GetAnalyticsResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlRestMapperTest {

    private final ShortUrlRestMapper mapper = ShortUrlRestMapper.INSTANCE;

    @Test
    void shouldMapToCreateShortUrlResponse() {
        CreateShortUrlResult result = new CreateShortUrlResult("abc", "https://example.com", Instant.now());
        CreateShortUrlResponse response = mapper.toResponse(result);

        assertNotNull(response);
        assertEquals(result.shortCode(), response.shortCode());
        assertEquals(result.originalUrl(), response.originalUrl());
        assertEquals(result.expiresAt(), response.expiresAt());
    }

    @Test
    void shouldMapToCreateShortUrlCommand() {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com", "abc");
        CreateShortUrlCommand command = mapper.toCommand(request);

        assertNotNull(command);
        assertEquals(request.originalUrl(), command.originalUrl());
        assertEquals(request.shortCode(), command.shortCode());
    }

    @Test
    void shouldMapToGetAnalyticsResponse() {
        UrlClickDTO click = new UrlClickDTO("abc", "127.0.0.1", "Agent", "Ref", Instant.now());
        AnalitycsResult result = new AnalitycsResult("abc", "https://example.com", Instant.now(), null, 10L, List.of(click));
        
        GetAnalyticsResponse response = mapper.toResponse(result);

        assertNotNull(response);
        assertEquals(result.shortCode(), response.getShortCode());
        assertEquals(result.originalUrl(), response.getOriginalUrl());
        assertEquals(result.clicks(), response.getClicks());
        assertEquals(1, response.getClicksDetails().size());
        assertEquals(click.ipAddress(), response.getClicksDetails().get(0).getIpAddress());
    }
}
