package ar.com.urlshortener.infrastructure.rest.controller;

import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.application.usecase.CreateShortUrlUseCase;
import ar.com.urlshortener.application.usecase.ResolveShortUrlUseCase;
import ar.com.urlshortener.infrastructure.rest.mapper.ShortUrlRestMapper;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlRequest;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlResponse;
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

    @PostMapping
    public ResponseEntity<CreateShortUrlResponse> createShortUrlUseCase(
            @Valid @RequestBody CreateShortUrlRequest request) {

        var command = ShortUrlRestMapper.INSTANCE.toCommand(request);
        var result = createShortUrlUseCase.execute(command);

        return ResponseEntity.ok(ShortUrlRestMapper.INSTANCE.toResponse(result));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
       String originalUrl = resolveShortUrlUseCase.execute(new ResolveShortUrlQuery(shortCode));

       return ResponseEntity
               .status(HttpStatus.FOUND) // 302
               .location(URI.create(originalUrl))
               .build();
    }


}
