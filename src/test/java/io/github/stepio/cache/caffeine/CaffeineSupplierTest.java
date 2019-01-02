package io.github.stepio.cache.caffeine;

import io.github.stepio.cache.caffeine.support.TestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CaffeineSupplier}.
 *
 * @author Igor Stepanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CaffeineSupplierTest {

    @Autowired
    private CaffeineSupplier cacheBuilderSupplier;

    @Test
    public void getCacheSpecificationKey_withDummy() {
        assertThat(this.cacheBuilderSupplier.composeKey("largeShort")).isEqualTo("spring.cache.caffeine.spec.largeShort");
        assertThat(this.cacheBuilderSupplier.composeKey("medium")).isEqualTo("spring.cache.caffeine.spec.medium");
        assertThat(this.cacheBuilderSupplier.composeKey("tinyLong")).isEqualTo("spring.cache.caffeine.spec.tinyLong");
        assertThat(this.cacheBuilderSupplier.composeKey("dummy")).isEqualTo("spring.cache.caffeine.spec.dummy");
    }

    @Test
    public void getCacheSpecification_withDummy() throws Exception {
        assertThat(this.cacheBuilderSupplier.apply("largeShort"))
                .hasFieldOrPropertyWithValue("maximumSize", 10_000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 5_000_000_000L);
        assertThat(this.cacheBuilderSupplier.apply("medium"))
                .hasFieldOrPropertyWithValue("maximumSize", 2000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 120_000_000_000L);
        assertThat(this.cacheBuilderSupplier.apply("tinyLong"))
                .hasFieldOrPropertyWithValue("maximumSize", 10L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 21600_000_000_000L);
        assertThat(this.cacheBuilderSupplier.apply("default"))
                .hasFieldOrPropertyWithValue("maximumSize", 5000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 300_000_000_000L);
        assertThat(this.cacheBuilderSupplier.apply("dummy"))
                .hasFieldOrPropertyWithValue("maximumSize", 5000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 300_000_000_000L);
    }
}
