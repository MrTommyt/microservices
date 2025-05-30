package co.edu.unimagdalena.cbenavides.gateway.configuration;

import co.edu.unimagdalena.cbenavides.gateway.components.manager.ReactiveCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class ReactiveCacheManagerConfig {

    @Bean
    public ReactiveCacheManager reactiveCacheManager(@Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        return new ReactiveCacheManager() {
            @Override
            public Mono<String> get(String key) {
                return redisTemplate.opsForValue().get(key);
            }

            @Override
            public Mono<Boolean> set(String key, String value, Duration ttl) {
                return redisTemplate.opsForValue().set(key, value, ttl);
            }

            @Override
            public Mono<Boolean> delete(String key) {
                return redisTemplate.delete(key).map(count -> count > 0);
            }
        };
    }
}