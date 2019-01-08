package io.github.stepio.cache.caffeine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * AutoConfiguration to create all the necessary beans.
 *
 * @author Igor Stepanov
 */
public class CaffeineSpecSpringAutoConfiguration {

    /**
     * Create CaffeineSupplier bean if it's not available in the context.
     * @return the CaffeineSupplier instance
     */
    @Bean
    @ConditionalOnMissingBean
    public CaffeineSupplier caffeineSupplier() {
        return new CaffeineSupplier();
    }

    /**
     * Create MultiConfigurationCacheManager bean if CacheManager is not available in the context.
     * @param caffeineSupplier the underlying CaffeineSupplier bean
     * @return the MultiConfigurationCacheManager instance
     */
    @Bean
    @ConditionalOnMissingBean
    public MultiConfigurationCacheManager cacheManager(CaffeineSupplier caffeineSupplier) {
        MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
        cacheManager.setCacheBuilderSupplier(caffeineSupplier);
        return cacheManager;
    }
}
