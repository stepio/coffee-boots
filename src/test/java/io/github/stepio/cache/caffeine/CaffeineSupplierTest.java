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
    private CaffeineSupplier caffeineSupplier;

    @Test
    public void getCacheSpecificationKey_withDummy() {
        assertThat(this.caffeineSupplier.composeKey("largeShort")).isEqualTo("spring.cache.caffeine.spec.largeShort");
        assertThat(this.caffeineSupplier.composeKey("medium")).isEqualTo("spring.cache.caffeine.spec.medium");
        assertThat(this.caffeineSupplier.composeKey("tinyLong")).isEqualTo("spring.cache.caffeine.spec.tinyLong");
        assertThat(this.caffeineSupplier.composeKey("dummy")).isEqualTo("spring.cache.caffeine.spec.dummy");
    }

    @Test
    public void getCacheSpecification_withDummy() throws Exception {
        assertThat(this.caffeineSupplier.apply("largeShort"))
                .hasFieldOrPropertyWithValue("maximumSize", 10_000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 5_000_000_000L);
        assertThat(this.caffeineSupplier.apply("medium"))
                .hasFieldOrPropertyWithValue("maximumSize", 2000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 120_000_000_000L);
        assertThat(this.caffeineSupplier.apply("tinyLong"))
                .hasFieldOrPropertyWithValue("maximumSize", 10L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 21600_000_000_000L);
        assertThat(this.caffeineSupplier.apply("default"))
                .hasFieldOrPropertyWithValue("maximumSize", 5000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 300_000_000_000L);
        assertThat(this.caffeineSupplier.apply("dummy"))
                .hasFieldOrPropertyWithValue("maximumSize", 5000L)
                .hasFieldOrPropertyWithValue("expireAfterWriteNanos", 300_000_000_000L);
    }
}
