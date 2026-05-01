package ar.com.urlshortener.infrastructure.rest.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GetAnalyticsResponse {

    private String shortCode;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;
    private long clicks;
    private List<UrlClickResponse> clicksDetails;

}
