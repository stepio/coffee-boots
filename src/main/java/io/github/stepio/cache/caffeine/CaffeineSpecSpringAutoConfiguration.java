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
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration to create all the necessary beans.
 *
 * @author Igor Stepanov
 */
@Configuration
@ConditionalOnClass({CacheManager.class, Caffeine.class})
@ConditionalOnMissingBean(
        value = {CacheManager.class},
        name = {"cacheResolver"}
)
@AutoConfigureBefore({CacheAutoConfiguration.class})
public class CaffeineSpecSpringAutoConfiguration {

    /**
     * Create CaffeineSupplier bean if it's not available in the context.
     * @return the CaffeineSupplier instance
     */
    @Bean
    @ConditionalOnMissingBean
    public CaffeineSupplier caffeineSupplier() {
        return new CaffeineSupplier();
    }

    /**
     * Create MultiConfigurationCacheManager bean if CacheManager is not available in the context.
     * @param caffeineSupplier the underlying CaffeineSupplier bean
     * @return the MultiConfigurationCacheManager instance
     */
    @Bean
    @ConditionalOnMissingBean
    public MultiConfigurationCacheManager cacheManager(CaffeineSupplier caffeineSupplier) {
        MultiConfigurationCacheManager cacheManager = new MultiConfigurationCacheManager();
        cacheManager.setCacheBuilderSupplier(caffeineSupplier);
        return cacheManager;
    }
}
