package ar.com.urlshortener.domain.model;

import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ShortUrl {

    private Long id;
    private ShortCode shortCode;
    private OriginalUrl originalUrl;
    private Instant createAt;
    private Instant expiresAt;
    private long clicks;

    public Boolean isExpired(Instant now) {
        return !Objects.isNull(expiresAt) && now.isAfter(expiresAt);
    }

    public void registerClick(){
        this.clicks++;
    }

    public static ShortUrl create(ShortCode shortCode, String originalUrl, Instant expiresAt) {
        return new ShortUrl(
                null,
                shortCode,
                new OriginalUrl(originalUrl),
                Instant.now(),
                expiresAt,
                0L
        );
    }

}
