package ar.com.urlshortener.application.command;

import java.time.Instant;

public record CreateShortUrlCommand(
        String originalUrl
) {

}
