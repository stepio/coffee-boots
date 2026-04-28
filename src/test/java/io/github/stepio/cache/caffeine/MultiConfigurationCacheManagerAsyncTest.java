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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(
        classes = {MultiConfigurationCacheManagerAsyncTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class MultiConfigurationCacheManagerAsyncTest {

    @Autowired
    private CaffeineSupplier caffeineSupplier;
    @Autowired
    private CachedDataHolder cachedDataHolder;

    @BeforeEach
    void setUp() {
        Caffeine<Object, Object> custom = Caffeine.newBuilder()
                .expireAfterWrite(200L, TimeUnit.MILLISECONDS)
                .maximumSize(5L);
        this.caffeineSupplier.putCaffeine("asyncCustom", custom);
        this.caffeineSupplier.putCaffeineSpecification("asyncSpec", "maximumSize=1000,expireAfterWrite=1h");
        this.caffeineSupplier.putCaffeineSpecification("asyncCompletableFuture", "maximumSize=5,expireAfterWrite=PT0.1S");
    }

    @Test
    void testCreateAsyncCaffeineCacheWithProgrammaticCaffeine() {
        Object aCustom = this.cachedDataHolder.newCachedCustomObject();
        assertThat(this.cachedDataHolder.newCachedCustomObject()).isSameAs(aCustom);

        final Object etalon = aCustom;
        await().atMost(300, TimeUnit.MILLISECONDS)
                .until(() -> !this.cachedDataHolder.newCachedCustomObject().equals(etalon));

        aCustom = this.cachedDataHolder.newCachedCustomObject();
        assertThat(this.cachedDataHolder.newCachedCustomObject()).isSameAs(aCustom);
    }

    @Test
    void testCreateAsyncCaffeineCacheWithPropertySpec() {
        Object aSpec = this.cachedDataHolder.newCachedSpecObject();
        assertThat(this.cachedDataHolder.newCachedSpecObject()).isSameAs(aSpec);
    }

    @Test
    void testCreateAsyncCompletableFuturePropertySpec() throws ExecutionException, InterruptedException, TimeoutException {
        Object aSpec = this.cachedDataHolder.newCachedCompletableFutureObject().get(10, TimeUnit.MILLISECONDS);
        assertThat(this.cachedDataHolder.newCachedCompletableFutureObject().get(10, TimeUnit.MILLISECONDS)).isSameAs(aSpec);

        final Object etalon = aSpec;
        await().atMost(200, TimeUnit.MILLISECONDS)
                .until(() -> !this.cachedDataHolder.newCachedCompletableFutureObject().get(10, TimeUnit.MILLISECONDS).equals(etalon));

        aSpec = this.cachedDataHolder.newCachedCompletableFutureObject().get(10, TimeUnit.MILLISECONDS);
        assertThat(this.cachedDataHolder.newCachedCompletableFutureObject().get(10, TimeUnit.MILLISECONDS)).isSameAs(aSpec);
    }

    @Test
    void testCreateAsyncCaffeineCacheFallbackToDefault() {
        Object aFallback = this.cachedDataHolder.newCachedFallbackObject();
        assertThat(this.cachedDataHolder.newCachedFallbackObject()).isSameAs(aFallback);
    }

    @SpringBootApplication
    @EnableCaching
    static class TestContext {

        @Bean
        public CaffeineSpecResolver caffeineSpecResolver() {
            return new CaffeineSpecResolver();
        }

        @Bean
        public CaffeineSupplier caffeineSupplier(CaffeineSpecResolver caffeineSpecResolver) {
            return new CaffeineSupplier(caffeineSpecResolver);
        }

        @Bean
        public MultiConfigurationCacheManager multiCaffeineManager(CaffeineSupplier caffeineSupplier) {
            MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
            cacheManager.setCacheBuilderSupplier(caffeineSupplier);
            cacheManager.setAsyncCacheMode(true);
            return cacheManager;
        }

        @Bean
        public CachedDataHolder cachedDataHolder() {
            return new CachedDataHolder();
        }
    }

    protected static class CachedDataHolder {

        @Cacheable("asyncCustom")
        public Object newCachedCustomObject() {
            return new Object();
        }

        @Cacheable("asyncSpec")
        public Object newCachedSpecObject() {
            return new Object();
        }

        @Cacheable("asyncFallback")
        public Object newCachedFallbackObject() {
            return new Object();
        }

        @Cacheable("asyncCompletableFuture")
        public CompletableFuture<Object> newCachedCompletableFutureObject() {
            return CompletableFuture.completedFuture(new Object());
        }
    }
}
