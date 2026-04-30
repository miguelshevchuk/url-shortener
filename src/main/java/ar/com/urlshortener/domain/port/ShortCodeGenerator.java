package ar.com.urlshortener.domain.port;

import ar.com.urlshortener.domain.model.vo.ShortCode;

public interface ShortCodeGenerator {

    ShortCode generate();

}
