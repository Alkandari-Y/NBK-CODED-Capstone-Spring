package com.project.authentication.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCacheManager

@TestConfiguration
class NoCacheTestConfig {
    @Bean
    fun cacheManager(): CacheManager = NoOpCacheManager()
}
