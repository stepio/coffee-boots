package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cache builder supplier.
 */
public interface CacheBuilderSupplier {
    /**
     * Create a cache builder for the cache with the given name.
     * @param name Cache name.
     * @return Cache builder.
     */
    Caffeine<Object, Object> cacheBuilder(String name);
}
