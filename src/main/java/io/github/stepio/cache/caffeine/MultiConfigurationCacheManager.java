package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.function.Function;

/**
 * Extension for Spring's {@link org.springframework.cache.caffeine.CaffeineCacheManager}.
 * Supports different Caffeine cache specifications per cache.
 *
 * @author Igor Stepanov
 */
public class MultiConfigurationCacheManager extends CaffeineCacheManager {

    private Function<String, Caffeine<Object, Object>> cacheBuilderSupplier;

    /**
     * Set the {@link Function} to produce {@link com.github.benmanes.caffeine.cache.Cache}
     * to use for building each individual {@link com.github.benmanes.caffeine.cache.Cache} instance.
     * @see #createNativeCaffeineCache
     * @see Caffeine#from(CaffeineSpec)
     */
    public void setCaffeineSupplier(Function<String, Caffeine<Object, Object>> supplier) {
        this.cacheBuilderSupplier = supplier;
    }

    @Override
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        return this.cacheBuilderSupplier.apply(name).build();
    }
}
