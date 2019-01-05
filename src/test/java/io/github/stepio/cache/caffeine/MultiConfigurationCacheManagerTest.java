package io.github.stepio.cache.caffeine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MultiConfigurationCacheManagerTest.TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MultiConfigurationCacheManagerTest {

    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Test
    public void testCreateNativeCaffeineCacheAsDefault() {
        Object dummy = this.cachedDataHolder.newCachedSpecialObject();
        assertThat(this.cachedDataHolder.newCachedSpecialObject()).isSameAs(dummy);
    }

    @Test
    public void testCreateNativeCaffeineCacheWithPreconfiguredCaches() {
        Object longTerm = this.cachedDataHolder.newCachedLongTermObject();
        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);

        Object aShortTerm = this.cachedDataHolder.newShortTermObject();
        Object bShortTerm = this.cachedDataHolder.newShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        final Object etalon = aShortTerm;
        await().atMost(2100, TimeUnit.MILLISECONDS)
                .until(() -> this.cachedDataHolder.newCachedShortTermObject() != etalon);

        bShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);
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

        @Cacheable("special")
        public Object newCachedSpecialObject() {
            return new Object();
        }

        @Cacheable("tinyLong")
        public Object newCachedLongTermObject() {
            return new Object();
        }

        @Cacheable("largeShort")
        public Object newCachedShortTermObject() {
            return new Object();
        }

        @CachePut("largeShort")
        public Object newShortTermObject() {
            return new Object();
        }
    }
}
