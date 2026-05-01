package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.IpAddress;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.model.vo.UserAgent;
import ar.com.urlshortener.infrastructure.persistence.model.UrlClickEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlClickRepositoryImplTest {

    @Mock
    private UrlClickCRUDRepository urlClickCRUDRepository;

    @InjectMocks
    private UrlClickRepositoryImpl urlClickRepository;

    @Test
    void shouldSaveUrlClick() {
        UrlClick model = new UrlClick(
                null,
                new ShortCode("abc"),
                Instant.now(),
                new UserAgent("Mozilla"),
                new IpAddress("127.0.0.1"),
                "referer"
        );

        urlClickRepository.save(model);

        verify(urlClickCRUDRepository).save(any(UrlClickEntity.class));
    }

    @Test
    void shouldFindByShortUrl() {
        String code = "abc";
        UrlClickEntity entity = new UrlClickEntity(1L, code, Instant.now(), "Agent", "127.0.0.1", "Ref");
        
        when(urlClickCRUDRepository.findByShortCode(code)).thenReturn(List.of(entity));

        Iterable<UrlClick> result = urlClickRepository.findByShortUrl(new ShortCode(code));

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        assertEquals(code, result.iterator().next().getShortCode().value());
    }
}
