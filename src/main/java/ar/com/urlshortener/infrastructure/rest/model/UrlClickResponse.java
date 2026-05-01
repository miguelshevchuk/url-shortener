package ar.com.urlshortener.infrastructure.rest.model;

import lombok.Data;

import java.time.Instant;

@Data
public class UrlClickResponse {

    private String shortCode;
    private String ipAddress;
    private String userAgent;
    private String referer;
    private Instant timestamp;

}
