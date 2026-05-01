package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.infrastructure.persistence.model.ShortUrlEntity;
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
class ShortUrlRepositoryImplTest {

    @Mock
    private ShortUrlCRUDRepository shortUrlCRUDRepository;

    @InjectMocks
    private ShortUrlRepositoryImpl shortUrlRepository;

    @Test
    void shouldFindByShortCode() {
        String code = "abc";
        ShortUrlEntity entity = ShortUrlEntity.builder()
                .shortCode(code)
                .originalUrl("https://example.com")
                .build();
        
        when(shortUrlCRUDRepository.findByShortCode(code)).thenReturn(entity);

        ShortUrl result = shortUrlRepository.findByShortCode(new ShortCode(code));

        assertNotNull(result);
        assertEquals(code, result.getShortCode().value());
        verify(shortUrlCRUDRepository).findByShortCode(code);
    }

    @Test
    void shouldSaveShortUrl() {
        ShortUrl model = new ShortUrl(null, new ShortCode("abc"), new OriginalUrl("https://example.com"), Instant.now(), null, 0);
        ShortUrlEntity savedEntity = ShortUrlEntity.builder()
                .id(1L)
                .shortCode("abc")
                .originalUrl("https://example.com")
                .build();

        when(shortUrlCRUDRepository.save(any(ShortUrlEntity.class))).thenReturn(savedEntity);

        ShortUrl result = shortUrlRepository.save(model);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(shortUrlCRUDRepository).save(any(ShortUrlEntity.class));
    }

    @Test
    void shouldReturnTrueWhenExistsByShortCode() {
        String code = "abc";
        when(shortUrlCRUDRepository.findByShortCode(code)).thenReturn(new ShortUrlEntity());

        Boolean exists = shortUrlRepository.existsByShortCode(new ShortCode(code));

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByShortCode() {
        String code = "none";
        when(shortUrlCRUDRepository.findByShortCode(code)).thenReturn(null);

        Boolean exists = shortUrlRepository.existsByShortCode(new ShortCode(code));

        assertFalse(exists);
    }
}
