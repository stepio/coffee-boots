package io.github.stepio.cache.metrics;

import io.github.stepio.cache.CacheCustomizer;
import io.micrometer.core.instrument.Tag;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;

/**
 * Implementation of {@link CacheCustomizer}, which is used to bind newly created caches to metrics registry.
 */
public class MetricsRegistrarProxy implements CacheCustomizer {

    private CacheMetricsRegistrar cacheMetricsRegistrar;

    public MetricsRegistrarProxy(CacheMetricsRegistrar cacheMetricsRegistrar) {
        this.cacheMetricsRegistrar = cacheMetricsRegistrar;
    }

    @Override
    public void onCreate(String name, Cache cache) {
        Tag cacheManagerTag = Tag.of("cacheManager", "multiCaffeineManager");
        this.cacheMetricsRegistrar.bindCacheToRegistry(cache, cacheManagerTag);
    }
}
