package ar.com.urlshortener.infrastructure.persistence.mapper;

import ar.com.urlshortener.domain.model.UrlClick;
import ar.com.urlshortener.infrastructure.persistence.model.UrlClickEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UrlClickMapper {

    UrlClickMapper INSTANCE = Mappers.getMapper(UrlClickMapper.class);

    @Mapping(target = "shortCode", source = "shortCode.value")
    @Mapping(target = "userAgent", source = "userAgent.value")
    @Mapping(target = "ipAddress", source = "ipAddress.value")
    @Mapping(target = "referer", source = "referer")
    UrlClickEntity toEntity(UrlClick urlClick);

    @Mapping(target = "shortCode.value", source = "shortCode")
    @Mapping(target = "userAgent.value", source = "userAgent")
    @Mapping(target = "ipAddress.value", source = "ipAddress")
    @Mapping(target = "referer", source = "referer")
    UrlClick toModel(UrlClickEntity urlClickEntity);

    Iterable<UrlClick> toModel(Iterable<UrlClickEntity> urlClickEntities);

}
