package ar.com.urlshortener.domain.exception;

import org.springframework.http.HttpStatus;

public class ShortUrlExpiredException extends UrlShortenerException {
    public ShortUrlExpiredException(String shortCode) {
        super("Short URL expirada: " + shortCode, HttpStatus.GONE);
    }
}
