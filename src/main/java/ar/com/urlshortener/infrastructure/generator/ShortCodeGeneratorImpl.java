package ar.com.urlshortener.infrastructure.generator;

import ar.com.urlshortener.domain.model.vo.ShortCode;
import ar.com.urlshortener.domain.port.ShortCodeGenerator;
import lombok.AllArgsConstructor;

import java.security.SecureRandom;


@AllArgsConstructor
public class ShortCodeGeneratorImpl implements ShortCodeGenerator {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 8;
    private final SecureRandom random = new SecureRandom();

    @Override
    public ShortCode generate() {
        // Esta implementacion no es la ideal ya que es un Random y no asegura unicidad.
        // Se podria asegurar unicidad desde el use case, haciendo mas de un intento.
        // La solucion ideal es asegurar la unicidad utilizando como base un dato incremental.
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return new ShortCode(code.toString());
    }
}
