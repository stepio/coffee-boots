/*
 * Copyright (C) 2018 - 2019 Igor Stepanov. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.stepio.cache.caffeine;

import io.github.stepio.cache.CacheCustomizer;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@SpringBootTest(
        classes = {MultiConfigurationCacheManagerTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class MultiConfigurationCacheManagerTest {

    @Autowired
    private MultiConfigurationCacheManager cacheManager;
    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Test
    public void testSetCacheBuilderSupplier() {
        assertThat(this.cacheManager.cacheBuilderSupplier).isNotNull();
    }

    @Test
    public void testSetCustomizers() {
        assertThat(this.cacheManager.customizers).isNotEmpty();
    }

    @Test
    public void testCreateCaffeineCache() {
        AtomicInteger counter = new AtomicInteger();
        CacheCustomizer customizer = mock(CacheCustomizer.class);
        doAnswer(i -> {
            counter.incrementAndGet();
            return null;
        }).when(customizer).onCreate(any(), any());
        MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
        cacheManager.setCustomizers(Collections.singletonList(customizer));
        assertThat(counter.get()).isZero();
        cacheManager.createCaffeineCache("dummy");
        assertThat(counter.get()).isOne();
    }

    @Test
    public void testCreateNativeCaffeineCacheAsDefault() {
        assertThat(this.cacheManager.getCacheBuilderSupplier()).isNotNull();
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
