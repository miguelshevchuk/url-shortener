package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.domain.exception.ShortUrlExistsException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.infrastructure.config.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CreateShortUrlUseCase {

    private final ShortUrlRepository shortUrlRespository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final AppProperties properties;

    public CreateShortUrlResult execute(CreateShortUrlCommand command) {

        var shortCode = getShortCode(command);

        ShortUrl shortUrl = ShortUrl.create(
                shortCode,
                command.originalUrl(),
                Instant.now().plusSeconds(properties.getExpirationSeconds())
        );

        ShortUrl savedShortUrl = shortUrlRespository.save(shortUrl);

        return CreateShortUrlResult.from(savedShortUrl);
    }

    private ShortCode getShortCode(CreateShortUrlCommand command) {

        if(Objects.isNull(command.shortCode()) || command.shortCode().isBlank()){
            return shortCodeGenerator.generate();
        }

        var shortCode = new ShortCode(command.shortCode());

        if(shortUrlRespository.existsByShortCode(shortCode)){
            throw new ShortUrlExistsException(shortCode.value());
        }

        return shortCode;
    }

}
