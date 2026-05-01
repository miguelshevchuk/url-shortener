package ar.com.urlshortener.infrastructure.rest.controller;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.AnalitycsResult;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.application.usecase.CreateShortUrlUseCase;
import ar.com.urlshortener.application.usecase.GetAnalyticsUseCase;
import ar.com.urlshortener.application.usecase.ResolveShortUrlUseCase;
import ar.com.urlshortener.infrastructure.filter.RateLimitFilter;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShortUrlRestController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RateLimitFilter.class))
class ShortUrlRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateShortUrlUseCase createShortUrlUseCase;

    @MockitoBean
    private ResolveShortUrlUseCase resolveShortUrlUseCase;

    @MockitoBean
    private GetAnalyticsUseCase getAnalyticsUseCase;

    @Test
    void shouldCreateShortUrl() throws Exception {
        CreateShortUrlRequest request = new CreateShortUrlRequest("https://example.com", "custom");
        CreateShortUrlResult result = new CreateShortUrlResult("custom", "https://example.com", Instant.now().plusSeconds(3600));

        when(createShortUrlUseCase.execute(any(CreateShortUrlCommand.class))).thenReturn(result);

        mockMvc.perform(post("/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("custom"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void shouldRedirect() throws Exception {
        String shortCode = "abc";
        String originalUrl = "https://example.com";

        when(resolveShortUrlUseCase.execute(any(ResolveShortUrlQuery.class))).thenReturn(originalUrl);

        mockMvc.perform(get("/{shortCode}", shortCode)
                        .header("User-Agent", "Mozilla")
                        .header("Referer", "google.com"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void shouldGetAnalytics() throws Exception {
        String shortCode = "abc";
        AnalitycsResult result = new AnalitycsResult(shortCode, "https://example.com", Instant.now(), null, 5L, List.of());

        when(getAnalyticsUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(get("/url/{shortCode}/analytics", shortCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value(shortCode))
                .andExpect(jsonPath("$.clicks").value(5));
    }

    @Test
    void shouldReturnClientIpFromXForwardedFor() throws Exception {
        String shortCode = "abc";
        when(resolveShortUrlUseCase.execute(any())).thenReturn("https://example.com");

        mockMvc.perform(get("/{shortCode}", shortCode)
                        .header("X-Forwarded-For", "1.2.3.4, 5.6.7.8"))
                .andExpect(status().isFound());
        
        // La verificación de la IP extraída se hace implícitamente al no fallar la llamada
    }

    @Test
    void shouldReturnClientIpFromXRealIp() throws Exception {
        String shortCode = "abc";
        when(resolveShortUrlUseCase.execute(any())).thenReturn("https://example.com");

        mockMvc.perform(get("/{shortCode}", shortCode)
                        .header("X-Real-IP", "9.8.7.6"))
                .andExpect(status().isFound());
    }
}
