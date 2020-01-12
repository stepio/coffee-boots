package io.github.stepio.cache.metrics;

import io.github.stepio.cache.CacheCustomizer;
import io.micrometer.core.instrument.Tag;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;

public class MetricsRegistrarProxy implements CacheCustomizer {

    private CacheMetricsRegistrar cacheMetricsRegistrar;

    public MetricsRegistrarProxy(CacheMetricsRegistrar cacheMetricsRegistrar) {
        this.cacheMetricsRegistrar = cacheMetricsRegistrar;
    }

    @Override
    public void onCreate(Cache cache) {
        Tag cacheManagerTag = Tag.of("cacheManager", "multiCaffeineManager");
        this.cacheMetricsRegistrar.bindCacheToRegistry(cache, cacheManagerTag);
    }
}
