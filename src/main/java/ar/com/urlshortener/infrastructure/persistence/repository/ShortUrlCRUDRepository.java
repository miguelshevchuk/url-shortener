package ar.com.urlshortener.infrastructure.persistence.repository;

import ar.com.urlshortener.infrastructure.persistence.model.ShortUrlEntity;
import org.springframework.data.repository.CrudRepository;


public interface ShortUrlCRUDRepository extends CrudRepository<ShortUrlEntity, Long> {

    ShortUrlEntity findByShortCode(String shortCode);

}
