package ar.com.urlshortener.application.dto;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.infrastructure.persistence.model.UrlClickEntity;

import java.time.Instant;

public record UrlClickDTO(
        String shortCode,
        String ipAddress,
        String userAgent,
        String referer,
        Instant timestamp
) {

    public static UrlClickDTO from(UrlClick urlClick){
        return new UrlClickDTO(
                urlClick.getShortCode().value(),
                urlClick.getIpAddress().value(),
                urlClick.getUserAgent().value(),
                urlClick.getReferer(),
                urlClick.getTimestamp()
        );
    }

}
