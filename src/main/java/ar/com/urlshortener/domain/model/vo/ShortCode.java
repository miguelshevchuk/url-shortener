package ar.com.urlshortener.domain.model.vo;

import java.util.Objects;

public record ShortCode(String value) {

    public ShortCode {
        if(Objects.isNull(value) || value.isBlank()){
            throw new IllegalArgumentException("ShortCode no puede ser Null");
        }
    }

}
