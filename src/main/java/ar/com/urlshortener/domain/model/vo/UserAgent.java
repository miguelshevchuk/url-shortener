package ar.com.urlshortener.domain.model.vo;

import java.util.Objects;

public record UserAgent(String value) {
    public UserAgent {
        if(Objects.isNull(value) || value.isBlank()){
            throw new IllegalArgumentException("UserAgent no puede ser Null");
        }
    }
}
