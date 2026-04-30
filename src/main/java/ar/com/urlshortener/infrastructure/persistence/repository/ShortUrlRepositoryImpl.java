package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortUrlRespository;
import ar.com.urlshortener.infrastructure.persistence.mapper.ShortUrlMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ShortUrlRepositoryImpl implements ShortUrlRespository {

    private final ShortUrlCRUDRepository shortUrlCRUDRepository;

    @Override
    @Cacheable(value = "shortUrls", key = "#shortCode.value")
    public ShortUrl findByShortCode(ShortCode shortCode) {
        var shortUrlSaved = shortUrlCRUDRepository.findByShortCode(shortCode.value());
        return ShortUrlMapper.INSTANCE.toModel(shortUrlSaved);
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {

        var saved = this.shortUrlCRUDRepository
                .save(ShortUrlMapper.INSTANCE.toEntity(shortUrl));

        return ShortUrlMapper.INSTANCE.toModel(saved);
    }

    @Override
    public Boolean existsByShortCode(ShortCode shortCode) {
        var saved = this.shortUrlCRUDRepository.findByShortCode(shortCode.value());

        return Objects.nonNull(saved);
    }

}
