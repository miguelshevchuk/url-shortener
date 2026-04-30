package ar.com.urlshortener.infrastructure.rest.model;

import java.time.Instant;

public record CreateShortUrlResponse(
        String shortCode,
        String originalUrl,
        Instant expiresAt
) {
}
