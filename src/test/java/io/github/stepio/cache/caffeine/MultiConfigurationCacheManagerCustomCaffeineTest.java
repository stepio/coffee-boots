package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MultiConfigurationCacheManagerCustomCaffeineTest.TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MultiConfigurationCacheManagerCustomCaffeineTest {

    @Autowired
    private CaffeineSupplier caffeineSupplier;
    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Before
    public void setUp() {
        Caffeine<Object, Object> custom = Caffeine.<Object, Object>newBuilder()
                .expireAfterWrite(200L, TimeUnit.MILLISECONDS)
                .maximumSize(5L);
        this.caffeineSupplier.putCaffeine("custom", custom);
        this.caffeineSupplier.putCaffeineSpecification("largeShort", "maximumSize=1000,expireAfterWrite=1h");
    }

    @Test
    public void testCreateNativeCaffeineCacheWithCustomCaches() {
        Object aCustom = this.cachedDataHolder.newCachedCustomObject();
        assertThat(this.cachedDataHolder.newCachedCustomObject()).isSameAs(aCustom);

        final Object etalon = aCustom;
        await().atMost(300, TimeUnit.MILLISECONDS)
                .until(() -> this.cachedDataHolder.newCachedCustomObject() != etalon);

        aCustom = this.cachedDataHolder.newCachedCustomObject();
        assertThat(this.cachedDataHolder.newCachedCustomObject()).isSameAs(aCustom);
    }

    @Test
    public void testCreateNativeCaffeineCacheWithPreconfiguredCachesIgnoreCustom() {
        Object aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(this.cachedDataHolder.newCachedShortTermObject()).isSameAs(aShortTerm);

        final Object etalon = aShortTerm;
        await().atMost(2100, TimeUnit.MILLISECONDS)
                .until(() -> this.cachedDataHolder.newCachedShortTermObject() != etalon);

        assertThat(this.cachedDataHolder.newCachedShortTermObject()).isNotSameAs(aShortTerm);
    }

    @SpringBootApplication
    @EnableCaching
    static class TestContext {

        @Bean
        public CachedDataHolder cachedDataHolder() {
            return new CachedDataHolder();
        }
    }

    private static class CachedDataHolder {

        @Cacheable("custom")
        public Object newCachedCustomObject() {
            return new Object();
        }

        @Cacheable("largeShort")
        public Object newCachedShortTermObject() {
            return new Object();
        }
    }
}
