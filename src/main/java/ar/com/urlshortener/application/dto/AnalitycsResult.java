package ar.com.urlshortener.application.dto;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.UrlClick;

import java.time.Instant;
import java.util.List;
import java.util.stream.StreamSupport;

public record AnalitycsResult(
        String shortCode,
        String originalUrl,
        Instant createdAt,
        Instant expiresAt,
        long clicks,
        List<UrlClickDTO> clicksDetails
) {

    public static AnalitycsResult from(ShortUrl url, Iterable<UrlClick> clicks){

        List<UrlClickDTO> clicksDetails = StreamSupport
                .stream(clicks.spliterator(), false)
                .map(
                        UrlClickDTO::from
                ).toList();

        return new AnalitycsResult(
                url.getShortCode().value(),
                url.getOriginalUrl().value(),
                url.getCreateAt(),
                url.getExpiresAt(),
                url.getClicks(),
                clicksDetails
        );
    }

}
