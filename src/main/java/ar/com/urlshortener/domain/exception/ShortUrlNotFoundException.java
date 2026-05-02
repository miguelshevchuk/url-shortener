package ar.com.urlshortener.domain.exception;

import org.springframework.http.HttpStatus;

public class ShortUrlNotFoundException extends UrlShortenerException {
    public ShortUrlNotFoundException(String shortCode) {
        super("Short URL no encontrada: " + shortCode, HttpStatus.NOT_FOUND);
    }
}
