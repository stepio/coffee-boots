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

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(
        classes = {MultiConfigurationCacheManagerCustomCaffeineTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class MultiConfigurationCacheManagerCustomCaffeineTest {

    @Autowired
    private CaffeineSupplier caffeineSupplier;
    @Autowired
    private CachedDataHolder cachedDataHolder;

    @BeforeEach
    public void setUp() {
        Caffeine<Object, Object> custom = Caffeine.newBuilder()
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
                .until(() -> !this.cachedDataHolder.newCachedCustomObject().equals(etalon));

        aCustom = this.cachedDataHolder.newCachedCustomObject();
        assertThat(this.cachedDataHolder.newCachedCustomObject()).isSameAs(aCustom);
    }

    @Test
    public void testCreateNativeCaffeineCacheWithPreconfiguredCachesIgnoreCustom() {
        Object aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(this.cachedDataHolder.newCachedShortTermObject()).isSameAs(aShortTerm);

        final Object etalon = aShortTerm;
        await().atMost(2100, TimeUnit.MILLISECONDS)
                .until(() -> !this.cachedDataHolder.newCachedShortTermObject().equals(etalon));

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

    protected static class CachedDataHolder {

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
