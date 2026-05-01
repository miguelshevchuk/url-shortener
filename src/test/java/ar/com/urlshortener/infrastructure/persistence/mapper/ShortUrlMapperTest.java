package ar.com.urlshortener.infrastructure.persistence.mapper;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.infrastructure.persistence.model.ShortUrlEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlMapperTest {

    private final ShortUrlMapper mapper = ShortUrlMapper.INSTANCE;

    @Test
    void shouldMapToEntity() {
        ShortUrl model = new ShortUrl(
                1L,
                new ShortCode("abc"),
                new OriginalUrl("https://example.com"),
                Instant.now(),
                Instant.now().plusSeconds(3600),
                10L
        );

        ShortUrlEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getShortCode().value(), entity.getShortCode());
        assertEquals(model.getOriginalUrl().value(), entity.getOriginalUrl());
        assertEquals(model.getCreateAt(), entity.getCreateAt());
        assertEquals(model.getExpiresAt(), entity.getExpiresAt());
        assertEquals(model.getClicks(), entity.getClicks());
    }

    @Test
    void shouldMapToModel() {
        ShortUrlEntity entity = ShortUrlEntity.builder()
                .id(1L)
                .shortCode("abc")
                .originalUrl("https://example.com")
                .createAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .clicks(10L)
                .build();

        ShortUrl model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getShortCode(), model.getShortCode().value());
        assertEquals(entity.getOriginalUrl(), model.getOriginalUrl().value());
        assertEquals(entity.getCreateAt(), model.getCreateAt());
        assertEquals(entity.getExpiresAt(), model.getExpiresAt());
        assertEquals(entity.getClicks(), model.getClicks());
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        assertNull(mapper.toModel(null));
    }
}
