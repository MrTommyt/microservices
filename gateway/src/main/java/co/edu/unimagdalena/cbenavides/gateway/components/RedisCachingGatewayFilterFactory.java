package co.edu.unimagdalena.cbenavides.gateway.components;

import co.edu.unimagdalena.cbenavides.gateway.components.manager.ReactiveCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class RedisCachingGatewayFilterFactory extends AbstractGatewayFilterFactory<RedisCachingGatewayFilterFactory.Config> {

    @Autowired
    private final ReactiveCacheManager reactiveCacheManager;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public RedisCachingGatewayFilterFactory(ReactiveCacheManager reactiveCacheManager) {
        super(Config.class);
        this.reactiveCacheManager = reactiveCacheManager;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String[] parts = path.split("/");

            // La ruta esperada es: /api/v1/product/products/{id}, significa que parts[5] = id
            if (parts.length < 6) {
                return chain.filter(exchange); // Ruta no válida, seguir sin cache
            }

            String productId = parts[5];
            Duration ttl = Duration.ofMinutes(config.getTtlMinutes());

            // Usar ReactiveCacheManager para obtener el valor en caché
            return reactiveCacheManager.get(productId)
                    .flatMap(cachedProduct -> {
                        if (cachedProduct != null && !cachedProduct.isEmpty()) {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(cachedProduct.getBytes(StandardCharsets.UTF_8));
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        }

                        // Si no está en caché, llamar al microservicio
                        return webClientBuilder.build()
                                .get()
                                .uri("http://product-service/api/v1/product/products/" + productId)
                                .retrieve()
                                .bodyToMono(String.class)
                                .flatMap(product ->
                                        reactiveCacheManager.set(productId, product, ttl)
                                                .then(Mono.defer(() -> {
                                                    DataBuffer buffer = exchange.getResponse().bufferFactory()
                                                            .wrap(product.getBytes(StandardCharsets.UTF_8));
                                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                                    return exchange.getResponse().writeWith(Mono.just(buffer));
                                                }))
                                );
                    });
        };
    }

    public static class Config {
        private long ttlMinutes = 5;

        public long getTtlMinutes() {
            return ttlMinutes;
        }

        public void setTtlMinutes(long ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }
    }
}
