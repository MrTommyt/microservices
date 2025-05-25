package co.edu.unimagdalena.cbenavides.gateway.components;

import co.edu.unimagdalena.cbenavides.gateway.components.manager.ReactiveCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class RedisCachingGatewayFilterFactory extends AbstractGatewayFilterFactory<RedisCachingGatewayFilterFactory.Config> {

    private final ReactiveCacheManager reactiveCacheManager;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public RedisCachingGatewayFilterFactory(ReactiveCacheManager reactiveCacheManager) {
        this.reactiveCacheManager = reactiveCacheManager;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String[] parts = path.split("/");

            if (parts.length < 6) {
                return chain.filter(exchange);
            }

            String productId = parts[5];

            return reactiveCacheManager.get(productId)
                    .flatMap(cachedProduct -> {
                        if (cachedProduct != null) {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(cachedProduct.getBytes(StandardCharsets.UTF_8));
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        } else {
                            return webClientBuilder.build()
                                    .get()
                                    .uri("http://product-service/api/v1/product/products/" + productId)
                                    .retrieve()
                                    .bodyToMono(String.class)
                                    .flatMap(product -> reactiveCacheManager.set(productId, product, Duration.ofMinutes(5))
                                            .then(Mono.defer(() -> {
                                                DataBuffer buffer = exchange.getResponse().bufferFactory()
                                                        .wrap(product.getBytes(StandardCharsets.UTF_8));
                                                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                                return exchange.getResponse().writeWith(Mono.just(buffer));
                                            })));
                        }
                    });
        };
    }
    public static class Config {
    }
}
