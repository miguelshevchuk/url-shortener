package ar.com.urlshortener.domain.port;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.ShortCode;

public interface UrlClickRepository {

    void save(UrlClick urlClick);
    Iterable<UrlClick> findByShortUrl(ShortCode shortCode);

}
