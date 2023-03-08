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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {MultiConfigurationCacheManagerNoCaffeineSupplierTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class MultiConfigurationCacheManagerNoCaffeineSupplierTest {

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
        public MultiConfigurationCacheManager cacheManager() {
            return new MultiConfigurationCacheManager();
        }

        @Bean
        public CachedDataHolder cachedDataHolder() {
            return new CachedDataHolder();
        }
    }

    protected static class CachedDataHolder {

        @Cacheable("dummy")
        public Object newCachedDummyObject() {
            return new Object();
        }
    }
}
