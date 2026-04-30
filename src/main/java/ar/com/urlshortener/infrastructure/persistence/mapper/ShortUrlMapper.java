package ar.com.urlshortener.infrastructure.persistence.mapper;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.infrastructure.persistence.model.ShortUrlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface ShortUrlMapper {

    ShortUrlMapper INSTANCE = Mappers.getMapper(ShortUrlMapper.class);

    default ShortUrlEntity toEntity(ShortUrl shortUrl){

        return ShortUrlEntity.builder()
                .id(Optional.ofNullable(shortUrl.getId()).orElse(null))
                .shortCode(shortUrl.getShortCode().value())
                .originalUrl(shortUrl.getOriginalUrl().value())
                .clicks(shortUrl.getClicks())
                .createAt(shortUrl.getCreateAt())
                .expiresAt(shortUrl.getExpiresAt())
                .build();

    }

    default ShortUrl toModel(ShortUrlEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ShortUrl(
                entity.getId(),
                new ShortCode(entity.getShortCode()),
                new OriginalUrl(entity.getOriginalUrl()),
                entity.getCreateAt(),
                entity.getExpiresAt(),
                entity.getClicks()
        );
    }

}
