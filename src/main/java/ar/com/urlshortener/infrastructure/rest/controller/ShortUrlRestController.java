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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/short-url")
@AllArgsConstructor
public class ShortUrlRestController {

    private CreateShortUrlUseCase createShortUrlUseCase;
    private ResolveShortUrlUseCase resolveShortUrlUseCase;
    private GetAnalyticsUseCase getAnalyticsUseCase;

    @PostMapping
    public ResponseEntity<CreateShortUrlResponse> createShortUrlUseCase(
            @Valid @RequestBody CreateShortUrlRequest request) {

        var command = ShortUrlRestMapper.INSTANCE.toCommand(request);
        var result = createShortUrlUseCase.execute(command);

        return ResponseEntity.ok(ShortUrlRestMapper.INSTANCE.toResponse(result));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            HttpServletRequest request,
            @PathVariable String shortCode
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

    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<GetAnalyticsResponse> getAnalytics(
            @PathVariable String shortCode
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
