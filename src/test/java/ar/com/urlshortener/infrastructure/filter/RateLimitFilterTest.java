package ar.com.urlshortener.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitFilter rateLimitFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private StringWriter responseContent;

    @BeforeEach
    void setUp() throws IOException {
        rateLimitFilter = new RateLimitFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        responseContent = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseContent));
    }

    @Test
    void shouldAllowRequestUnderLimit() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        rateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @Test
    void shouldBlockRequestWhenLimitExceeded() throws ServletException, IOException {
        String clientIp = "192.168.1.2";
        when(request.getRemoteAddr()).thenReturn(clientIp);

        // El límite es 10 tokens por segundo
        for (int i = 0; i < 10; i++) {
            rateLimitFilter.doFilter(request, response, filterChain);
        }

        // El 11vo request debería ser bloqueado
        reset(filterChain);
        rateLimitFilter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(response).setContentType("application/json");
    }

    @Test
    void shouldExtractIpFromXForwardedForHeader() throws ServletException, IOException {
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.195, 70.41.3.18");
        
        // Consumimos el limite para esta IP especifica
        for (int i = 0; i < 10; i++) {
            rateLimitFilter.doFilter(request, response, filterChain);
        }
        
        // Verificamos que se aplico a la primera IP de la lista
        reset(filterChain);
        rateLimitFilter.doFilter(request, response, filterChain);
        
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        
        // Una IP distinta no deberia estar bloqueada
        HttpServletRequest request2 = mock(HttpServletRequest.class);
        when(request2.getRemoteAddr()).thenReturn("1.1.1.1");
        FilterChain filterChain2 = mock(FilterChain.class);
        
        rateLimitFilter.doFilter(request2, response, filterChain2);
        verify(filterChain2).doFilter(request2, response);
    }
}
