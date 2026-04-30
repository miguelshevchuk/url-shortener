package ar.com.urlshortener.domain.port;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;

import java.util.Optional;

public interface ShortUrlRespository {

    ShortUrl findByShortCode(ShortCode shortCode);
    ShortUrl save(ShortUrl shortUrl);
    Boolean existsByShortCode(ShortCode shortCode);
}
