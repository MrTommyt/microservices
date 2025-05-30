package co.edu.unimagdalena.cbenavides.gateway.components.manager;

import reactor.core.publisher.Mono;

import java.time.Duration;

public interface ReactiveCacheManager {
    Mono<String> get(String key);
    Mono<Boolean> set(String key, String value, Duration ttl);
    Mono<Boolean> delete(String key);
}
