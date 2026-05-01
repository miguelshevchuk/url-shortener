package ar.com.urlshortener.infrastructure.rest.controller;

import ar.com.urlshortener.application.query.GetAnalitycsQuery;
import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.application.usecase.CreateShortUrlUseCase;
import ar.com.urlshortener.application.usecase.GetAnalyticsUseCase;
import ar.com.urlshortener.application.usecase.ResolveShortUrlUseCase;
import ar.com.urlshortener.infrastructure.rest.mapper.ShortUrlRestMapper;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlRequest;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlResponse;
import ar.com.urlshortener.infrastructure.rest.model.GetAnalyticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controlador REST para gestionar URLs cortas.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Short URL", description = "API para creación, resolución y analíticas de URLs cortas")
public class ShortUrlRestController {

    private CreateShortUrlUseCase createShortUrlUseCase;
    private ResolveShortUrlUseCase resolveShortUrlUseCase;
    private GetAnalyticsUseCase getAnalyticsUseCase;

    /**
     * Crea una nueva URL corta.
     *
     * @param request Datos para la creación de la URL corta.
     * @return La URL corta creada.
     */
    @Operation(summary = "Crear URL corta", description = "Genera un código corto para una URL original dada.")
    @ApiResponse(responseCode = "200", description = "URL corta creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "409", description = "El código corto ya existe")
    @PostMapping("/url")
    public ResponseEntity<CreateShortUrlResponse> createShortUrlUseCase(
            @Valid @RequestBody CreateShortUrlRequest request) {

        var command = ShortUrlRestMapper.INSTANCE.toCommand(request);
        var result = createShortUrlUseCase.execute(command);

        return ResponseEntity.ok(ShortUrlRestMapper.INSTANCE.toResponse(result));
    }

    /**
     * Redirige a la URL original asociada al código corto.
     *
     * @param request   Petición HTTP.
     * @param shortCode Código corto de la URL.
     * @return Redirección HTTP 302 a la URL original.
     */
    @Operation(summary = "Redirigir", description = "Redirige a la URL original y registra el click.")
    @ApiResponse(responseCode = "302", description = "Redirección a la URL original")
    @ApiResponse(responseCode = "404", description = "URL corta no encontrada")
    @ApiResponse(responseCode = "410", description = "URL corta expirada")
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            HttpServletRequest request,
            @Parameter(description = "Código corto de la URL") @PathVariable String shortCode
    ) {
       String originalUrl = resolveShortUrlUseCase.execute(
               new ResolveShortUrlQuery(
                       shortCode,
                       getClientIP(request),
                       request.getHeader("User-Agent"),
                       request.getHeader("Referer")
               )
       );

       return ResponseEntity
               .status(HttpStatus.FOUND) // 302
               .location(URI.create(originalUrl))
               .build();
    }

    /**
     * Obtiene las analíticas de una URL corta.
     *
     * @param shortCode Código corto de la URL.
     * @return Información detallada de analíticas.
     */
    @Operation(summary = "Obtener analíticas", description = "Obtiene estadísticas de uso para una URL corta específica.")
    @ApiResponse(responseCode = "200", description = "Analíticas obtenidas exitosamente")
    @ApiResponse(responseCode = "404", description = "URL corta no encontrada")
    @GetMapping("/url/{shortCode}/analytics")
    public ResponseEntity<GetAnalyticsResponse> getAnalytics(
            @Parameter(description = "Código corto de la URL") @PathVariable String shortCode
    ) {

        var result = getAnalyticsUseCase.execute(new GetAnalitycsQuery(shortCode));

        return ResponseEntity.ok(ShortUrlRestMapper.INSTANCE.toResponse(result));
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            // X-Forwarded-For puede tener múltiples IPs: "client, proxy1, proxy2"
            return xfHeader.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }


}
