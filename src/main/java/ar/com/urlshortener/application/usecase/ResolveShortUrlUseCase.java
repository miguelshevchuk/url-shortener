package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.query.ResolveShortUrlQuery;
import ar.com.urlshortener.domain.exception.ShortUrlExpiredException;
import ar.com.urlshortener.domain.exception.ShortUrlNotFoundException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.IpAddress;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.model.vo.UserAgent;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class ResolveShortUrlUseCase {

    private final ShortUrlRepository shortUrlRespository;
    private final UrlClickRepository urlClickRepository;

    @Transactional
    public String execute(ResolveShortUrlQuery query) {

        ShortCode shortCode = new ShortCode(query.shortUrl());
        ShortUrl shortUrl = shortUrlRespository.findByShortCode(shortCode);

        validateShortUrl(shortUrl, shortCode);

        //En entornos productivos, lo ideal es que esto se maneje con eventos
        registerClick(query, shortUrl);

        return shortUrl.getOriginalUrl().value();

    }

    private void registerClick(ResolveShortUrlQuery query, ShortUrl shortUrl) {

        log.info("Registrando click {} en la URL: {}",  shortUrl.getShortCode().value(), shortUrl.getOriginalUrl().value());
        UrlClick urlClick = UrlClick.create(
            new ShortCode(query.shortUrl()),
                Instant.now(),
                new UserAgent(query.userAgent()),
                new IpAddress(query.ipAddress()),
                query.referer()
        );
        log.info("Datos del click: {}", urlClick);
        urlClickRepository.save(urlClick);
        shortUrl.registerClick();
        shortUrlRespository.save(shortUrl);

    }

    private static void validateShortUrl(ShortUrl shortUrl, ShortCode shortCode) {
        if(Objects.isNull(shortUrl)){
            log.error("Short URL no encontrada: {}", shortCode.value());
            throw new ShortUrlNotFoundException(shortCode.value());
        }

        if(shortUrl.isExpired(Instant.now())){
            log.error("Short URL expirada: {}", shortCode.value());
            throw new ShortUrlExpiredException(shortCode.value());
        }
    }

}
