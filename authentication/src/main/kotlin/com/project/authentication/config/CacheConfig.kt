package com.project.authentication.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            )
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues()
    }

    @Bean
    fun cacheManager(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisCacheManager {
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration())
            .build()
    }
}
