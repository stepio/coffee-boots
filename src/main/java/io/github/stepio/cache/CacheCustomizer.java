package io.github.stepio.cache;

import org.springframework.cache.Cache;

/**
 * Allows additional post-processing for newly created {@link Cache} instances.
 *
 * @author Igor Stepanov
 */
public interface CacheCustomizer {

    /**
     * Invoked upon creating a {@link Cache} instance.
     * @param name the name of the cache
     * @param cache recently created Cache instance
     * @see io.github.stepio.cache.caffeine.MultiConfigurationCacheManager#createCaffeineCache(String)
     */
    void onCreate(String name, Cache cache);
}
