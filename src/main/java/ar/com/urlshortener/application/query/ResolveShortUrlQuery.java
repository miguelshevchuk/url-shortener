package ar.com.urlshortener.application.query;

public record ResolveShortUrlQuery (
        String shortUrl,
        String ipAddress,
        String userAgent,
        String referer
) {
}
