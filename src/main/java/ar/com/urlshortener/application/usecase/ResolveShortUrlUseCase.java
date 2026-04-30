package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.domain.exception.ShortUrlExpiredException;
import ar.com.urlshortener.domain.exception.ShortUrlNotFoundException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortUrlRespository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ResolveShortUrlUseCase {

    private final ShortUrlRespository shortUrlRespository;

    @Transactional
    public String execute(ResolveShortUrlQuery query) {

        ShortCode shortCode = new ShortCode(query.shortUrl());

        ShortUrl shortUrl = shortUrlRespository.findByShortCode(shortCode);
        if(Objects.isNull(shortUrl)){
            log.error("Short URL no encontrada: {}", shortCode.value());
            throw new ShortUrlNotFoundException(shortCode.value());
        }

        if(shortUrl.isExpired(Instant.now())){
            log.error("Short URL expirada: {}", shortCode.value());
            throw new ShortUrlExpiredException(shortCode.value());
        }

        shortUrl.registerClick();
        shortUrlRespository.save(shortUrl);

        return shortUrl.getOriginalUrl().value();

    }

}
