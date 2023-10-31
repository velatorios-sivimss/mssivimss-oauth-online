package com.imss.sivimss.oauth.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@EnableCaching
@Configuration
public class CacheConfiguration implements CachingConfigurer {

	@Value("${tiempo-vida}") 
	private Integer tiempo;
	
	@Override
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name,
                    CacheBuilder.newBuilder().expireAfterWrite(tiempo, TimeUnit.MINUTES).maximumSize(100).build().asMap(), false);
            }
        };

        return cacheManager;
    }

}
