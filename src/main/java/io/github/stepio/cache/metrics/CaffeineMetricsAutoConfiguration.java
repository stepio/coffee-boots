package io.github.stepio.cache.metrics;

import io.github.stepio.cache.CacheCustomizer;
import io.github.stepio.cache.caffeine.MultiConfigurationCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnClass({CacheMetricsRegistrar.class})
public class CaffeineMetricsAutoConfiguration {

    @Autowired
    public void bind(MultiConfigurationCacheManager cacheManager, CacheMetricsRegistrar metricsRegistrar) {
        CacheCustomizer customizer = new MetricsRegistrarProxy(metricsRegistrar);
        cacheManager.setCustomizers(Collections.singletonList(customizer));
    }
}
