package com.colak.springcacheredistutorial.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    // To configure Redis Cache we have these options
    // 1. Use RedisCacheManagerBuilderCustomizer
    // 2. Create a CacheManager using RedisCacheConfiguration  + RedisCacheManager.RedisCacheManagerBuilder
    // Example is here
    // https://github.com/tugayesilyurt/spring-debezium-kafka-mysql-redis-cacheable/blob/main/spring-debezium-kafka-redis-cacheable/src/main/java/com/debezium/example/configuration/RedisConfig.java


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer();

        // We need to Randomize cache expiry periods
        // See https://medium.com/@shaileshkumarmishra/can-cache-layer-slow-down-databases-b72e70df18a8
        return builder -> builder
                .withCacheConfiguration("employees",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                )
                .withCacheConfiguration("restaurants",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(3))
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                );
    }
}
