package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.UrlClickRepository;
import ar.com.urlshortener.infrastructure.persistence.mapper.UrlClickMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementación del repositorio de clicks utilizando JPA.
 */
@Repository
@AllArgsConstructor
public class UrlClickRepositoryImpl implements UrlClickRepository {

    private final UrlClickCRUDRepository urlClickCRUDRepository;

    /**
     * Persiste la información de un nuevo click.
     *
     * @param urlClick Datos del click a guardar.
     */
    @Override
    public void save(UrlClick urlClick) {
        urlClickCRUDRepository.save(UrlClickMapper.INSTANCE.toEntity(urlClick));
    }

    /**
     * Recupera todos los clicks asociados a un código corto.
     *
     * @param shortCode Código corto para filtrar los clicks.
     * @return Iterable con los modelos de dominio UrlClick.
     */
    @Override
    public Iterable<UrlClick> findByShortUrl(ShortCode shortCode) {

        var saved = urlClickCRUDRepository.findByShortCode(shortCode.value());

        return UrlClickMapper.INSTANCE.toModel(saved);
    }
}
