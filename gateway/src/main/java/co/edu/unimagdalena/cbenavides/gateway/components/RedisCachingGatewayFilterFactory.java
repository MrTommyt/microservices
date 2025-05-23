package co.edu.unimagdalena.cbenavides.gateway.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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

            // La ruta esperada es: /api/v1/product/products/{id}, significa que parts[5] = id
            if (parts.length < 6) {
                return chain.filter(exchange); // Ruta no válida, seguir sin cache
            }

            String productId = parts[5];
            Cache cache = reactiveCacheManager.getCache("productsCache");

            if (cache != null) {
                String cachedProduct = cache.get(productId, String.class);

                if (cachedProduct != null) {
                    DataBuffer buffer = exchange.getResponse().bufferFactory()
                            .wrap(cachedProduct.getBytes(StandardCharsets.UTF_8));
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                }
            }

            // Si no está en cache entonces hacer la llamada al microservicio de productos
            return webClientBuilder.build()
                    .get()
                    .uri("http://product-service/api/v1/product/products/" + productId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(product -> {
                        if (product != null && cache != null) {
                            cache.put(productId, product);
                        }

                        DataBuffer buffer = exchange.getResponse().bufferFactory()
                                .wrap(product.getBytes(StandardCharsets.UTF_8));
                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    });
        };
    }

    public static class Config {
    }
}
