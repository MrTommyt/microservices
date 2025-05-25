package co.edu.unimagdalena.cbenavides.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RedisCacheService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisCacheService(@Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<String> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> set(String key, String value, Duration ttl) {
        return redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Mono<Boolean> delete(String key) {
        return redisTemplate.delete(key).map(count -> count > 0);
    }
}
