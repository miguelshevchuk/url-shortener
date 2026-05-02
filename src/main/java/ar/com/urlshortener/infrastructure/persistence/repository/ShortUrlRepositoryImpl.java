package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.ShortUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortUrlRepository;
import ar.com.urlshortener.infrastructure.persistence.mapper.ShortUrlMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * Implementación del repositorio de URLs cortas utilizando JPA y Caché.
 */
@Repository
@AllArgsConstructor
public class ShortUrlRepositoryImpl implements ShortUrlRepository {

    private final ShortUrlCRUDRepository shortUrlCRUDRepository;

    /**
     * Busca una URL corta por su código único.
     * Utiliza caché para optimizar las lecturas.
     *
     * @param shortCode Código corto a buscar.
     * @return Modelo de dominio ShortUrl o null si no se encuentra.
     */
    @Override
    @Cacheable(value = "shortUrls", key = "#shortCode.value")
    public ShortUrl findByShortCode(ShortCode shortCode) {
        var shortUrlSaved = shortUrlCRUDRepository.findByShortCode(shortCode.value());
        return ShortUrlMapper.INSTANCE.toModel(shortUrlSaved);
    }

    /**
     * Guarda o actualiza una URL corta.
     *
     * @param shortUrl Modelo de dominio a guardar.
     * @return El modelo guardado con su ID asignado.
     */
    @Override
    public ShortUrl save(ShortUrl shortUrl) {

        var saved = this.shortUrlCRUDRepository
                .save(ShortUrlMapper.INSTANCE.toEntity(shortUrl));

        return ShortUrlMapper.INSTANCE.toModel(saved);
    }

    /**
     * Verifica si ya existe un código corto registrado.
     *
     * @param shortCode Código a verificar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public Boolean existsByShortCode(ShortCode shortCode) {
        var saved = this.shortUrlCRUDRepository.findByShortCode(shortCode.value());

        return Objects.nonNull(saved);
    }

}
