package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.infrastructure.config.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CreateShortUrlUseCase {

    private final ShortUrlRepository shortUrlRespository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final AppProperties properties;

    public CreateShortUrlResult execute(CreateShortUrlCommand command) {

        var shortCode = shortCodeGenerator.generate();

        ShortUrl shortUrl = ShortUrl.create(
                shortCode,
                command.originalUrl(),
                Instant.now().plusSeconds(properties.getExpirationSeconds())
        );

        ShortUrl savedShortUrl = shortUrlRespository.save(shortUrl);

        return CreateShortUrlResult.from(savedShortUrl);
    }

}
