package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
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
     * Set the {@link Function} to produce {@link Caffeine} for building each individual {@link Cache} instance.
     * @param supplier the implementation of Caffeine producer
     * @see #createNativeCaffeineCache
     * @see Caffeine#from(CaffeineSpec)
     */
    public void setCacheBuilderSupplier(Function<String, Caffeine<Object, Object>> supplier) {
        this.cacheBuilderSupplier = supplier;
    }

    /**
     * Accessor for the underlying {@link Caffeine} producer.
     * @return exact implementation of function for instantiating the Cache with the specified name
     */
    public Function<String, Caffeine<Object, Object>> getCacheBuilderSupplier() {
        return cacheBuilderSupplier;
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     * If the appropriate custom Caffeine is available for the given name, applies it.
     * Otherwise uses Spring's implementation with common Caffeine by default.
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (this.cacheBuilderSupplier != null) {
            Caffeine<Object, Object> caffeine = this.cacheBuilderSupplier.apply(name);
            if (caffeine != null) {
                return caffeine.build();
            }
        }
        return super.createNativeCaffeineCache(name);
    }
}
