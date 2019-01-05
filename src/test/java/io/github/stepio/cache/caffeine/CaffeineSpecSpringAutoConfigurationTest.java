package io.github.stepio.cache.caffeine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CaffeineSpecSpringAutoConfigurationTest.TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CaffeineSpecSpringAutoConfigurationTest {

    @Autowired
    private MultiConfigurationCacheManager cacheManager;
    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Test
    public void testCreateNativeCaffeineCacheAsDefault() {
        assertThat(this.cacheManager.getCacheBuilderSupplier()).isNull();
        Object dummy = this.cachedDataHolder.newCachedDummyObject();
        assertThat(this.cachedDataHolder.newCachedDummyObject()).isSameAs(dummy);
    }

    @SpringBootApplication
    @EnableCaching
    static class TestContext {

        @Bean
        public MultiConfigurationCacheManager cacheManager(CaffeineSupplier caffeineSupplier) {
            return new MultiConfigurationCacheManager();
        }

        @Bean
        public CachedDataHolder cachedDataHolder() {
            return new CachedDataHolder();
        }
    }

    private static class CachedDataHolder {

        @Cacheable("dummy")
        public Object newCachedDummyObject() {
            return new Object();
        }
    }
}
