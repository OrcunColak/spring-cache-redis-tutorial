package com.colak.springtutorial.config;

import com.colak.springtutorial.employee.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    private static final String CACHE_PREFIX = "Cache:";

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {

        // See https://medium.com/@davenkin_93074/configure-jackson-when-using-spring-boot-redis-cache-daf658ea5e74
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper
                .activateDefaultTyping(cacheObjectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // Here we are using GenericJackson2JsonRedisSerializer together with Jackson’s Default Typing feature.
        // The GenericJackson2JsonRedisSerializer is supposed to work with any class.
        GenericJackson2JsonRedisSerializer genericSerializer = new GenericJackson2JsonRedisSerializer(cacheObjectMapper);

        // I would rather create Jackson2JsonRedisSerializer for each cached model to ensure our own model's freedom
        Jackson2JsonRedisSerializer<EmployeeDTO> employeeSerializer = new Jackson2JsonRedisSerializer<>(cacheObjectMapper, EmployeeDTO.class);

        // We need to Randomize cache expiry periods
        // See https://medium.com/@shaileshkumarmishra/can-cache-layer-slow-down-databases-b72e70df18a8
        return builder -> builder
                .transactionAware()
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .prefixCacheNameWith(CACHE_PREFIX)
                        .entryTtl(Duration.ofMinutes(60))
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericSerializer)))

                // cache name and RedisCacheConfiguration
                .withCacheConfiguration("employees",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(employeeSerializer))
                );
    }
}
