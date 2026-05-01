package ar.com.urlshortener.infrastructure.persistence.mapper;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.infrastructure.persistence.model.ShortUrlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

/**
 * Mapper de MapStruct para convertir entre el modelo de dominio ShortUrl y su entidad de persistencia.
 */
@Mapper
public interface ShortUrlMapper {

    /**
     * Instancia única del mapper.
     */
    ShortUrlMapper INSTANCE = Mappers.getMapper(ShortUrlMapper.class);

    /**
     * Convierte un modelo de dominio ShortUrl a su correspondiente entidad de base de datos.
     *
     * @param shortUrl Modelo de dominio.
     * @return Entidad de persistencia.
     */
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

    /**
     * Convierte una entidad de base de datos ShortUrlEntity al modelo de dominio ShortUrl.
     *
     * @param entity Entidad de persistencia.
     * @return Modelo de dominio ShortUrl.
     */
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
