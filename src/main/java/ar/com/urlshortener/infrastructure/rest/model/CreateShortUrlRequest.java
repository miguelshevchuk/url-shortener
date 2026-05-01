package ar.com.urlshortener.infrastructure.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

/**
 * Petición para crear una URL corta.
 */
@Schema(description = "Datos para crear una URL corta")
public record CreateShortUrlRequest(
        @Schema(description = "URL original a acortar", example = "https://www.google.com")
        @NotBlank
        @URL
        String originalUrl,
        @Schema(description = "Código corto personalizado (opcional)", example = "google")
        String shortCode
) {



}
