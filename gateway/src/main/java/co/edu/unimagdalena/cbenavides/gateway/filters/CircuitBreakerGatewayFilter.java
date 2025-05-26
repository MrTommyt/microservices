package co.edu.unimagdalena.cbenavides.gateway.filters;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class CircuitBreakerGatewayFilter implements GatewayFilter, Ordered {

    private final CircuitBreaker circuitBreaker;

    public CircuitBreakerGatewayFilter() {
        // Configurar el Circuit Breaker
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)                 // Porcentaje de fallos para abrir el circuito
                .waitDurationInOpenState(Duration.ofMillis(10000))  // Tiempo en estado abierto
                .slidingWindowSize(10)                    // Tamaño de la ventana deslizante
                .permittedNumberOfCallsInHalfOpenState(5) // Llamadas permitidas en estado semi-abierto
                .recordExceptions(Exception.class)        // Excepciones a registrar
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("serviceCircuitBreaker");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Estado del Circuit Breaker: {}", circuitBreaker.getState());

        // Si el circuito está abierto, devuelve error de servicio no disponible
        if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
            exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            return exchange.getResponse().setComplete();
        }

        // Aplicar el operador de circuit breaker al flujo reactivo
        return chain.filter(exchange)
                .transform(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorResume(throwable -> {
                    log.error("Error en la llamada al servicio: {}", throwable.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public int getOrder() {
        // Ejecutar después del filtro de CorrelationId
        return 0;
    }
}