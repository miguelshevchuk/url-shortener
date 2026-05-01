package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.domain.exception.ShortUrlExistsException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.infrastructure.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
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
class CreateShortUrlUseCaseTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private AppProperties properties;

    @InjectMocks
    private CreateShortUrlUseCase createShortUrlUseCase;

    @BeforeEach
    void setUp() {
        lenient().when(properties.getExpirationSeconds()).thenReturn(3600);
    }

    @Test
    void shouldCreateShortUrlWithCustomCode() {
        String customCode = "custom";
        String originalUrl = "https://example.com";
        CreateShortUrlCommand command = new CreateShortUrlCommand(originalUrl, customCode);

        when(shortUrlRepository.existsByShortCode(new ShortCode(customCode))).thenReturn(false);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateShortUrlResult result = createShortUrlUseCase.execute(command);

        assertNotNull(result);
        assertEquals(customCode, result.shortCode());
        assertEquals(originalUrl, result.originalUrl());
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void shouldCreateShortUrlWithGeneratedCode() {
        String originalUrl = "https://example.com";
        String generatedCode = "gen123";
        CreateShortUrlCommand command = new CreateShortUrlCommand(originalUrl, null);

        when(shortCodeGenerator.generate()).thenReturn(new ShortCode(generatedCode));
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateShortUrlResult result = createShortUrlUseCase.execute(command);

        assertNotNull(result);
        assertEquals(generatedCode, result.shortCode());
        verify(shortCodeGenerator).generate();
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomCodeExists() {
        String customCode = "exists";
        CreateShortUrlCommand command = new CreateShortUrlCommand("https://example.com", customCode);

        when(shortUrlRepository.existsByShortCode(new ShortCode(customCode))).thenReturn(true);

        assertThrows(ShortUrlExistsException.class, () -> createShortUrlUseCase.execute(command));
        verify(shortUrlRepository, never()).save(any(ShortUrl.class));
    }
}
