package ar.com.urlshortener.infrastructure.rest.mapper;

import ar.com.urlshortener.application.command.CreateShortUrlCommand;
import ar.com.urlshortener.application.dto.CreateShortUrlResult;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlRequest;
import ar.com.urlshortener.infrastructure.rest.model.CreateShortUrlResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShortUrlRestMapper {

    ShortUrlRestMapper INSTANCE = Mappers.getMapper(ShortUrlRestMapper.class);

    CreateShortUrlResponse toResponse(CreateShortUrlResult createShortUrlResult);

    CreateShortUrlCommand toCommand(CreateShortUrlRequest createShortUrlRequest);

}
