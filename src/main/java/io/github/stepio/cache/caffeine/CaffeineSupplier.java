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
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

/**
 * Supplier for {@link Caffeine} instances, constructed from {@link com.github.benmanes.caffeine.cache.CaffeineSpec} values.
 * Allows configuring different expiration, capacity and other caching parameters for different cache names.
 *
 * @author Igor Stepanov
 */
public class CaffeineSupplier implements CacheBuilderSupplier {

    protected CaffeineSpecResolver caffeineSpecResolver;
    protected final ConcurrentMap<String, Caffeine<Object, Object>> cacheBuilders = new ConcurrentHashMap<>(16);

    public CaffeineSupplier(CaffeineSpecResolver caffeineSpecResolver) {
        this.caffeineSpecResolver = caffeineSpecResolver;
    }

    /**
     * Entry point for programmatic customization of individual named Cache.
     * @param name the name of the cache
     * @param caffeine the Caffeine instance
     */
    public void putCaffeine(String name, Caffeine<Object, Object> caffeine) {
        notNull(caffeine, "Non-null Caffeine is mandatory");
        this.cacheBuilders.put(name, caffeine);
    }

    /**
     * Entry point for programmatic customization of individual named Cache.
     * @param name the name of the cache
     * @param caffeineSpec the CaffeineSpec instance for Caffeine instantiation
     */
    public void putCaffeineSpec(String name, CaffeineSpec caffeineSpec) {
        notNull(caffeineSpec, "Non-null Caffeine specification is mandatory");
        putCaffeine(name, Caffeine.from(caffeineSpec));
    }

    /**
     * Entry point for programmatic customization of individual named Cache.
     * @param name the name of the cache
     * @param caffeineSpecification the String representation of Caffeine specification
     */
    public void putCaffeineSpecification(String name, String caffeineSpecification) {
        hasText(caffeineSpecification, "Non-empty Caffeine specification is mandatory");
        putCaffeineSpec(name, CaffeineSpec.parse(caffeineSpecification));
    }

    @Override
    public Caffeine<Object, Object> cacheBuilder(String name) {
        String value = this.caffeineSpecResolver.getCaffeineSpec(name);
        if (StringUtils.hasText(value)) {
            return Caffeine.from(value);
        }
        return cacheBuilders.get(name);
    }
}
