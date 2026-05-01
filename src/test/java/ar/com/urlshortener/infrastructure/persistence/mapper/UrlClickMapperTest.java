package ar.com.urlshortener.infrastructure.persistence.mapper;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.IpAddress;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.model.vo.UserAgent;
import ar.com.urlshortener.infrastructure.persistence.model.UrlClickEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrlClickMapperTest {

    private final UrlClickMapper mapper = UrlClickMapper.INSTANCE;

    @Test
    void shouldMapToEntity() {
        UrlClick model = new UrlClick(
                1L,
                new ShortCode("abc"),
                Instant.now(),
                new UserAgent("Mozilla"),
                new IpAddress("127.0.0.1"),
                "google.com"
        );

        UrlClickEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getShortCode().value(), entity.getShortCode());
        assertEquals(model.getTimestamp(), entity.getTimestamp());
        assertEquals(model.getUserAgent().value(), entity.getUserAgent());
        assertEquals(model.getIpAddress().value(), entity.getIpAddress());
        assertEquals(model.getReferer(), entity.getReferer());
    }

    @Test
    void shouldMapToModel() {
        UrlClickEntity entity = new UrlClickEntity(
                1L,
                "abc",
                Instant.now(),
                "Mozilla",
                "127.0.0.1",
                "google.com"
        );

        UrlClick model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getShortCode(), model.getShortCode().value());
        assertEquals(entity.getTimestamp(), model.getTimestamp());
        assertEquals(entity.getUserAgent(), model.getUserAgent().value());
        assertEquals(entity.getIpAddress(), model.getIpAddress().value());
        assertEquals(entity.getReferer(), model.getReferer());
    }

    @Test
    void shouldMapIterableToModel() {
        UrlClickEntity entity1 = new UrlClickEntity(1L, "abc", Instant.now(), "M1", "127.0.0.1", "R1");
        UrlClickEntity entity2 = new UrlClickEntity(2L, "def", Instant.now(), "M2", "127.0.0.2", "R2");

        Iterable<UrlClick> models = mapper.toModel(List.of(entity1, entity2));

        assertNotNull(models);
        List<UrlClick> modelList = (List<UrlClick>) models;
        assertEquals(2, modelList.size());
        assertEquals(entity1.getShortCode(), modelList.get(0).getShortCode().value());
        assertEquals(entity2.getShortCode(), modelList.get(1).getShortCode().value());
    }
}
