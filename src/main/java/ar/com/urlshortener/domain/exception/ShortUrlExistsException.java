package ar.com.urlshortener.domain.exception;

public class ShortUrlExistsException extends UrlShortenerException {
    public ShortUrlExistsException(String shortCode) {
        super("Ya existe la Short URL: " + shortCode);
    }
}
