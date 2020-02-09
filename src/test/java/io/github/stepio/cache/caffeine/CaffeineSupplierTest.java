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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CaffeineSupplier}.
 *
 * @author Igor Stepanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {CaffeineSupplierTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class CaffeineSupplierTest {

    @Autowired
    private CaffeineSupplier caffeineSupplier;

    @Test
    public void testApplyWithPreconfiguredCaches() {
        assertThat(this.caffeineSupplier.apply("dummy")).isNull();
        assertThat(this.caffeineSupplier.apply("largeShort"))
                .hasFieldOrPropertyWithValue("maximumSize", 10_000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 2_000_000_000L);
        assertThat(this.caffeineSupplier.apply("medium"))
                .hasFieldOrPropertyWithValue("maximumSize", 2000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 120_000_000_000L);
        assertThat(this.caffeineSupplier.apply("tinyLong"))
                .hasFieldOrPropertyWithValue("maximumSize", 10L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 21600_000_000_000L);
    }

    @SpringBootApplication
    static class TestContext {
    }
}
