package co.edu.unimagdalena.cbenavides.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GatewayFilter, Ordered {
    private static final String CORRELATION_ID = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Verificar si ya viene un correlationId en los headers
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID);

        // Si no viene un correlationId entonces se genera uno nuevo
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(CORRELATION_ID, correlationId)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private String getCorrelationId(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(CORRELATION_ID);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}


