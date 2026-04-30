package ar.com.urlshortener.domain.exception;

public class UrlShortenerException extends RuntimeException {
    public UrlShortenerException(String msj) {
        super(msj);
    }
}
