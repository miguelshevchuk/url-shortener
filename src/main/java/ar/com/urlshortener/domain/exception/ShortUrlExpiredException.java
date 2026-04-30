package ar.com.urlshortener.domain.exception;

public class ShortUrlExpiredException extends UrlShortenerException {
    public ShortUrlExpiredException(String shortCode) {
        super("Short URL expirada: " + shortCode);
    }
}
