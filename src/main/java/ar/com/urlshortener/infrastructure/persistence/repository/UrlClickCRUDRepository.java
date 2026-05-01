package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.infrastructure.persistence.model.UrlClickEntity;
import org.springframework.data.repository.CrudRepository;

public interface UrlClickCRUDRepository extends CrudRepository<UrlClickEntity, Long> {

    Iterable<UrlClickEntity> findByShortCode(String shortCode);

}
