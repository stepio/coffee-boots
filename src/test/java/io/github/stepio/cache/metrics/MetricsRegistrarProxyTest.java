package io.github.stepio.cache.metrics;

import org.junit.Test;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.Cache;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class MetricsRegistrarProxyTest {

    @Test
    public void testOnCreate() {
        AtomicInteger counter = new AtomicInteger();
        CacheMetricsRegistrar cacheMetricsRegistrar = mock(CacheMetricsRegistrar.class);
        doAnswer(i -> {
            counter.incrementAndGet();
            return null;
        }).when(cacheMetricsRegistrar).bindCacheToRegistry(any(), any());
        MetricsRegistrarProxy metricsRegistrarProxy = new MetricsRegistrarProxy(cacheMetricsRegistrar);
        assertThat(counter.get()).isZero();
        metricsRegistrarProxy.onCreate("dummy", mock(Cache.class));
        assertThat(counter.get()).isOne();
    }
}
