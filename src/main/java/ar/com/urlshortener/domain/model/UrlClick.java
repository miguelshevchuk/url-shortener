package ar.com.urlshortener.domain.model;

import ar.com.urlshortener.domain.model.vo.IpAddress;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.model.vo.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UrlClick {

    private Long id;
    private ShortCode shortCode;
    private Instant timestamp;
    private UserAgent userAgent;
    private IpAddress ipAddress;
    private String referer;

    public static UrlClick create(ShortCode shortCode, Instant timestamp, UserAgent userAgent, IpAddress ipAddress, String referer) {
        return new UrlClick(null, shortCode, timestamp, userAgent, ipAddress, referer);
    }

}
