package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import ar.com.urlshortener.infrastructure.persistence.mapper.UrlClickMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UrlClickRepositoryImpl implements UrlClickRepository {

    private final UrlClickCRUDRepository urlClickCRUDRepository;

    @Override
    public void save(UrlClick urlClick) {
        urlClickCRUDRepository.save(UrlClickMapper.INSTANCE.toEntity(urlClick));
    }

    @Override
    public Iterable<UrlClick> findByShortUrl(ShortCode shortCode) {

        var saved = urlClickCRUDRepository.findByShortCode(shortCode.value());

        return UrlClickMapper.INSTANCE.toModel(saved);
    }
}
