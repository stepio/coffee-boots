package io.github.stepio.cache.metrics;

import io.github.stepio.cache.CacheCustomizer;
import io.github.stepio.cache.caffeine.MultiConfigurationCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Binds {@link CacheMetricsRegistrar} to {@link MultiConfigurationCacheManager}.
 * Allows configuring metrics for dynamic caches.
 *
 * @author Igor Stepanov
 */
@Configuration
@ConditionalOnClass({CacheMetricsRegistrar.class})
public class CaffeineMetricsAutoConfiguration {

    /**
     * Binds {@link CacheMetricsRegistrar} to {@link MultiConfigurationCacheManager} using {@link CacheCustomizer}.
     * @param cacheManager instance of MultiConfigurationCacheManager bean
     * @param metricsRegistrar instance of CacheMetricsRegistrar bean
     */
    @Autowired
    public void bind(MultiConfigurationCacheManager cacheManager, CacheMetricsRegistrar metricsRegistrar) {
        CacheCustomizer customizer = new MetricsRegistrarProxy(metricsRegistrar);
        cacheManager.setCustomizers(Collections.singletonList(customizer));
    }
}
