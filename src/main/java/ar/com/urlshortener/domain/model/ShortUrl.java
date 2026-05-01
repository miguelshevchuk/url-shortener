package ar.com.urlshortener.domain.model;

import ar.com.urlshortener.domain.model.vo.OriginalUrl;
import ar.com.urlshortener.domain.model.vo.ShortCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

/**
 * Representa una URL corta en el sistema.
 */
@Data
@AllArgsConstructor
public class ShortUrl {

    private Long id;
    private ShortCode shortCode;
    private OriginalUrl originalUrl;
    private Instant createAt;
    private Instant expiresAt;
    private long clicks;

    /**
     * Verifica si la URL ha expirado.
     *
     * @param now Marca de tiempo actual.
     * @return true si ha expirado, false en caso contrario.
     */
    public Boolean isExpired(Instant now) {
        return !Objects.isNull(expiresAt) && now.isAfter(expiresAt);
    }

    /**
     * Registra un nuevo click incrementando el contador.
     */
    public void registerClick(){
        this.clicks++;
    }

    /**
     * Crea una nueva instancia de ShortUrl con valores iniciales.
     *
     * @param shortCode   Código corto.
     * @param originalUrl URL original.
     * @param expiresAt   Fecha de expiración.
     * @return Nueva instancia de ShortUrl.
     */
    public static ShortUrl create(ShortCode shortCode, String originalUrl, Instant expiresAt) {
        return new ShortUrl(
                null,
                shortCode,
                new OriginalUrl(originalUrl),
                Instant.now(),
                expiresAt,
                0L
        );
    }

}
