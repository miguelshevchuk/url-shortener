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

/**
 * Caso de uso para la creación de URLs cortas.
 */
@Service
@AllArgsConstructor
public class CreateShortUrlUseCase {

    private final ShortUrlRepository shortUrlRespository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final AppProperties properties;

    /**
     * Ejecuta la lógica para crear una URL corta.
     * Si no se proporciona un código corto, se genera uno automáticamente.
     *
     * @param command Comando con la URL original y opcionalmente el código corto.
     * @return Resultado con la información de la URL corta creada.
     * @throws ShortUrlExistsException si el código corto ya está en uso.
     */
    public CreateShortUrlResult execute(CreateShortUrlCommand command) {

        var shortCode = getShortCode(command);

        ShortUrl shortUrl = ShortUrl.create(
                shortCode,
                command.originalUrl(),
                Instant.now().plusSeconds(properties.getExpirationSeconds())
        );

        ShortUrl savedShortUrl = shortUrlRespository.save(shortUrl);

        return CreateShortUrlResult.from(savedShortUrl, properties.getBaseUrl());
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
