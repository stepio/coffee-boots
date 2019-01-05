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
@SpringBootTest(classes = {CaffeineSupplierTest.TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CaffeineSupplierTest {

    @Autowired
    private CaffeineSupplier caffeineSupplier;

    @Test
    public void testComposeKeyWithDifferentCacheNames() {
        assertThat(this.caffeineSupplier.composeKey("dummy")).isEqualTo("spring.cache.caffeine.spec.dummy");
        assertThat(this.caffeineSupplier.composeKey("largeShort")).isEqualTo("spring.cache.caffeine.spec.largeShort");
        assertThat(this.caffeineSupplier.composeKey("medium")).isEqualTo("spring.cache.caffeine.spec.medium");
        assertThat(this.caffeineSupplier.composeKey("tinyLong")).isEqualTo("spring.cache.caffeine.spec.tinyLong");
    }

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
