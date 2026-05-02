package ar.com.urlshortener.infrastructure.rest.Exception;

import ar.com.urlshortener.domain.exception.UrlShortenerException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    void handleUrlShortenerException() {
        UrlShortenerException ex = new UrlShortenerException("Test error", HttpStatus.BAD_REQUEST);
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleShortenerException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test error", response.getBody().getDetail());
        assertEquals("/test-uri", response.getBody().getInstance().getPath());
    }

    @Test
    void handleGenericException() {
        Exception ex = new Exception("Generic error");
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleGeneric(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getDetail().contains("inesperado"));
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid arg");
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleIllegalArgument(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid arg", response.getBody().getDetail());
    }

    @Test
    void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "rejected", false, null, null, "message");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentNotValid(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getProperties().get("errors"));
    }
}
