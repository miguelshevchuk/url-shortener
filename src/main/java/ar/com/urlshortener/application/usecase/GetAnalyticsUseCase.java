package ar.com.urlshortener.application.usecase;

import ar.com.urlshortener.application.dto.AnalitycsResult;
import ar.com.urlshortener.application.query.GetAnalitycsQuery;
import ar.com.urlshortener.domain.exception.ShortUrlNotFoundException;
import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class GetAnalyticsUseCase {

    private final ShortUrlRepository shortUrlRespository;
    private final UrlClickRepository urlClickRepository;

    public AnalitycsResult execute(GetAnalitycsQuery query) {

        var shortCode = new ShortCode(query.shortCode());

        ShortUrl shortUrl = shortUrlRespository.findByShortCode(shortCode);
        if(Objects.isNull(shortUrl)){
            throw new ShortUrlNotFoundException(shortCode.value());
        }
        Iterable<UrlClick> clicks = urlClickRepository.findByShortUrl(shortCode);

        return AnalitycsResult.from(shortUrl, clicks);
    }

}
