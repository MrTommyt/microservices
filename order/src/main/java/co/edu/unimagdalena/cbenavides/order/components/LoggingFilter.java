package co.edu.unimagdalena.cbenavides.order.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class LoggingFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class.getName());
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getQueryParams()
            .entrySet()
            .stream()
            .map(en -> "%s: [%s]".formatted(
                en.getKey(),
                String.join(",", en.getValue())
            ))
            .collect(Collectors.joining("; ", "{", "}"));

        logger.info("Solicitud recibida: [{}] {}", method, path);

        return chain.filter(exchange)
            .doOnSuccess(aVoid -> logger.info("Respuesta enviada para: [{}] {}", method, path))
            .doOnCancel(() -> logger.info("Solicitud cancelada: [{}] {}", method, path))
            .doOnTerminate(() -> logger.info("Solicitud finalizada: [{}] {}", method, path))
            .doOnError(throwable -> logger.error("Error al procesar la solicitud: [{}] {}", method, path, throwable));
    }
}
