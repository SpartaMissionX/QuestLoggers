package com.missionx.questloggers.global.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        var caches = Arrays.stream(CacheType.values())
                .map(type -> new CaffeineCache(
                        type.getCacheName(),
                        Caffeine.newBuilder()
                                .expireAfterWrite(type.getExpireAfterWriteSec(), TimeUnit.SECONDS)
                                .maximumSize(type.getMaximumSize())
                                .recordStats()  // 히트/미스 통계 기록용
                                .build()
                ))
                .toList();

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
