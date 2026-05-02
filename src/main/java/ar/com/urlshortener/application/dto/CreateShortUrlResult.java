package ar.com.urlshortener.application.dto;

import ar.com.urlshortener.domain.model.ShortUrl;

import java.time.Instant;

public record CreateShortUrlResult(
        String shortCode,
        String shortUrl,
        String originalUrl,
        Instant expiresAt
) {

    public static CreateShortUrlResult from(ShortUrl shortUrl, String baseUrl){
        return new CreateShortUrlResult(
                shortUrl.getShortCode().value(),
                baseUrl+"/"+shortUrl.getShortCode().value(),
                shortUrl.getOriginalUrl().value(),
                shortUrl.getExpiresAt()
        );
    }

}
