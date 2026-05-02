package ar.com.urlshortener.domain.exception;

import org.springframework.http.HttpStatus;

public class ShortUrlExistsException extends UrlShortenerException {
    public ShortUrlExistsException(String shortCode) {
        super("Ya existe la Short URL: " + shortCode, HttpStatus.CONFLICT);
    }
}
