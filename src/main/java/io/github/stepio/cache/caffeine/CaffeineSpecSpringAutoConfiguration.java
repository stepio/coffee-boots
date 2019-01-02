package io.github.stepio.cache.caffeine;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({CacheProperties.class})
public class CaffeineSpecSpringAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CaffeineSupplier caffeineSupplier(CacheProperties cacheProperties) {
        return new CaffeineSupplier(cacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(CaffeineSupplier caffeineSupplier) {
        MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
        cacheManager.setCaffeineSupplier(caffeineSupplier);
        return cacheManager;
    }
}
