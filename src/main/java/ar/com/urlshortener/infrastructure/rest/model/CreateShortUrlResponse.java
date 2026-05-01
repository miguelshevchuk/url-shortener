package ar.com.urlshortener.infrastructure.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Respuesta con la información de la URL corta creada.
 */
@Schema(description = "Respuesta de creación de URL corta")
public record CreateShortUrlResponse(
        @Schema(description = "Código corto generado o personalizado")
        String shortCode,
        @Schema(description = "URL original")
        String originalUrl,
        @Schema(description = "Fecha de expiración")
        Instant expiresAt
) {
}
