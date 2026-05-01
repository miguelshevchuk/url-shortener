package ar.com.urlshortener.infrastructure.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    //Esta solucion es solo para el ejercicio.
    // Lo ideal es tener este tipo de soluciones a nivel Gateway, en una arquitectura basada en microservicios.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 1. Obtener la IP del usuario
        String clientIp = getClientIP(httpRequest);
        // 2. Obtener o crear el bucket para esa IP
        Bucket bucket = cache.computeIfAbsent(clientIp, this::createBucket);
        // 3. Intentar consumir 1 token
        if (bucket.tryConsume(1)) {
            //AY TOKENS: Dejar pasar el request
            chain.doFilter(request, response);
        } else {
            //NO HAY TOKENS: Rechazar con 429
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(
                    "{\"error\": \"Demasiadas solicitudes. Intentelo denuevo mas tarde.\"}"
            );
        }
    }

    // Crear bucket: 10 tokens, se recargan 10 cada 1 segundo
    private Bucket createBucket(String ip) {
        Bandwidth limit = Bandwidth.classic(
                10,                              // Capacidad: 10 tokens
                Refill.intervally(10, Duration.ofSeconds(1))  // Recarga: 10 tokens/segundo
        );
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // Obtener IP real (considerando proxies)
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}
