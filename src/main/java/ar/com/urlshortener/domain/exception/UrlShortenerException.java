package ar.com.urlshortener.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Getter
public class UrlShortenerException extends RuntimeException {

    private final HttpStatus status;

    public UrlShortenerException(String msj, HttpStatus status) {
        super(msj);
        this.status = Objects.isNull(status) ? HttpStatus.NOT_ACCEPTABLE : status;
    }

}
