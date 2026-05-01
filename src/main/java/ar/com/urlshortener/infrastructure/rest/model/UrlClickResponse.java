package ar.com.urlshortener.infrastructure.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

/**
 * Detalle de un click en una URL corta.
 */
@Data
@Schema(description = "Detalle de un click")
public class UrlClickResponse {

    @Schema(description = "Código corto")
    private String shortCode;
    @Schema(description = "Dirección IP del cliente")
    private String ipAddress;
    @Schema(description = "User Agent del cliente")
    private String userAgent;
    @Schema(description = "Referer de la petición")
    private String referer;
    @Schema(description = "Marca de tiempo del click")
    private Instant timestamp;

}
