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
     * Set the {@link Function} to produce {@link Cache}
     * to use for building each individual {@link Cache} instance.
     * @param supplier sets implementation of function for instantiating the {@link Cache} with the specified name
     * @see #createNativeCaffeineCache
     * @see Caffeine#from(CaffeineSpec)
     */
    public void setCacheBuilderSupplier(Function<String, Caffeine<Object, Object>> supplier) {
        this.cacheBuilderSupplier = supplier;
    }

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
