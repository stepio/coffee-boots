package io.github.stepio.cache.caffeine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * AutoConfiguration to create all the necessary beans.
 *
 * @author Igor Stepanov
 */
public class CaffeineSpecSpringAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CaffeineSupplier caffeineSupplier() {
        return new CaffeineSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(CaffeineSupplier caffeineSupplier) {
        MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
        cacheManager.setCacheBuilderSupplier(caffeineSupplier);
        return cacheManager;
    }
}
