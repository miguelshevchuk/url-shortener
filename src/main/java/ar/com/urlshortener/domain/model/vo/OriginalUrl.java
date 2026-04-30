package ar.com.urlshortener.domain.model.vo;

import java.util.Objects;

public record OriginalUrl(
        String value
) {

    public OriginalUrl {
        if(Objects.isNull(value) || value.isBlank()){
            throw new IllegalArgumentException("La URL original no puede ser Null");
        }

        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            throw new IllegalArgumentException("Original URL debe comenzar con http:// o https://");
        }
    }

}
