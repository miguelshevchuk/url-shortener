package ar.com.urlshortener.domain.exception;

public class ShortUrlNotFoundException extends UrlShortenerException {
    public ShortUrlNotFoundException(String shortCode) {
        super("Short URL no encontrada: " + shortCode);
    }
}
