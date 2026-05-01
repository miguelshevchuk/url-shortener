package ar.com.urlshortener.infrastructure.rest.model;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
        @NotBlank
        @URL
        String originalUrl,
        String shortCode
) {



}
