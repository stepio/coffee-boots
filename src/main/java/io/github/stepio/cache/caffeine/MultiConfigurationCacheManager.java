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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import io.github.stepio.cache.CacheCustomizer;
import io.github.stepio.cache.Customizable;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.List;

/**
 * Extension for Spring's {@link org.springframework.cache.caffeine.CaffeineCacheManager}.
 * Supports different Caffeine cache specifications per cache.
 *
 * @author Igor Stepanov
 */
public class MultiConfigurationCacheManager extends CaffeineCacheManager implements Customizable {

    protected CacheBuilderSupplier cacheBuilderSupplier;
    protected List<CacheCustomizer> customizers;

    /**
     * Set the {@link CacheBuilderSupplier} to produce {@link Caffeine} for building each individual {@link Cache} instance.
     * @param supplier the implementation of Caffeine producer
     * @see #createNativeCaffeineCache
     * @see Caffeine#from(CaffeineSpec)
     */
    public void setCacheBuilderSupplier(CacheBuilderSupplier supplier) {
        this.cacheBuilderSupplier = supplier;
    }

    public void setCustomizers(List<CacheCustomizer> customizers) {
        this.customizers = customizers;
    }

    /**
     * Accessor for the underlying {@link Caffeine} producer.
     * @return exact implementation of function for instantiating the Cache with the specified name
     */
    public CacheBuilderSupplier getCacheBuilderSupplier() {
        return this.cacheBuilderSupplier;
    }

    @Override
    protected org.springframework.cache.Cache createCaffeineCache(String name) {
        org.springframework.cache.Cache cache = super.createCaffeineCache(name);
        if (this.customizers != null) {
            this.customizers.forEach(customizer -> customizer.onCreate(name, cache));
        }
        return cache;
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     * If the appropriate custom Caffeine is available for the given name, applies it.
     * Otherwise uses Spring's implementation with common Caffeine by default.
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (this.cacheBuilderSupplier != null) {
            Caffeine<Object, Object> caffeine = this.cacheBuilderSupplier.cacheBuilder(name);
            if (caffeine != null) {
                return caffeine.build();
            }
        }
        return super.createNativeCaffeineCache(name);
    }
}
