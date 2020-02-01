package io.github.stepio.cache.metrics;

import io.github.stepio.cache.CacheCustomizer;
import io.github.stepio.cache.Customizable;
import io.github.stepio.cache.caffeine.MultiConfigurationCacheManager;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * Binds {@link CacheMetricsRegistrar} to {@link MultiConfigurationCacheManager}.
 * Allows configuring metrics for dynamic caches.
 * @see org.springframework.boot.actuate.autoconfigure.metrics.cache.CacheMetricsRegistrarConfiguration
 *
 * @author Igor Stepanov
 */
@Configuration
@ConditionalOnClass({CacheMetricsRegistrar.class})
public class CaffeineMetricsAutoConfiguration {

    private static final String CACHE_MANAGER_SUFFIX = "cacheManager";

    /**
     * Binds {@link CacheMetricsRegistrar} to the applicable {@link CacheManager} instances.
     * Applicable means instances implementing {@link Customizable}.
     * @param cacheManagers map of CacheManager beans
     * @param metricsRegistrar instance of CacheMetricsRegistrar bean
     */
    public CaffeineMetricsAutoConfiguration(Map<String, CacheManager> cacheManagers,
                                                 CacheMetricsRegistrar metricsRegistrar) {
        cacheManagers.forEach((name, manager) ->
                bindCacheToRegistry(name, manager, metricsRegistrar));
    }

    private void bindCacheToRegistry(String beanName,
                                     CacheManager cacheManager,
                                     CacheMetricsRegistrar metricsRegistrar) {
        if (cacheManager instanceof Customizable) {
            CacheCustomizer customizer =
                    new MetricsRegistrarProxy(metricsRegistrar, getCacheManagerName(beanName));
            Customizable customizable = (Customizable) cacheManager;
            customizable.setCustomizers(Collections.singletonList(customizer));
        }
    }

    /**
     * @see org.springframework.boot.actuate.autoconfigure.metrics.cache.CacheMetricsRegistrarConfiguration#getCacheManagerName(String)
     */
    private String getCacheManagerName(String beanName) {
        if (beanName.length() > CACHE_MANAGER_SUFFIX.length()
                && StringUtils.endsWithIgnoreCase(beanName, CACHE_MANAGER_SUFFIX)) {
            return beanName.substring(0, beanName.length() - CACHE_MANAGER_SUFFIX.length());
        }
        return beanName;
    }
}
