package ar.com.urlshortener.infrastructure.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Respuesta con las analíticas de una URL corta.
 */
@Data
@Schema(description = "Respuesta con analíticas de una URL corta")
public class GetAnalyticsResponse {

    @Schema(description = "Código corto de la URL")
    private String shortCode;
    @Schema(description = "URL original")
    private String originalUrl;
    @Schema(description = "Fecha de creación")
    private Instant createdAt;
    @Schema(description = "Fecha de expiración")
    private Instant expiresAt;
    @Schema(description = "Número total de clicks")
    private long clicks;
    @Schema(description = "Detalle de los últimos clicks")
    private List<UrlClickResponse> clicksDetails;

}
