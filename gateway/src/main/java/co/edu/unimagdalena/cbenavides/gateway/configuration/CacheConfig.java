package co.edu.unimagdalena.cbenavides.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {

    @Bean(name = "2reactiveRedisTemplate")
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveRedisTemplate<>(
                connectionFactory,
                RedisSerializationContext.<String, String>newSerializationContext(new StringRedisSerializer())
                        .key(new StringRedisSerializer())
                        .value(new StringRedisSerializer())
                        .hashKey(new StringRedisSerializer())
                        .hashValue(new StringRedisSerializer())
                        .build()
        );
    }

    @Bean
    public ReactiveCacheManager reactiveCacheManager(ReactiveRedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return new org.springframework.data.redis.cache.ReactiveRedisCacheManager(
                factory, cacheConfiguration);
    }
}