package io.github.stepio.cache.caffeine;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CaffeineSpecResolverTest {

    @Test
    public void testComposeKeyWithDifferentCacheNames() {
        assertThat(CaffeineSpecResolver.composeKey("dummy")).isEqualTo("coffee-boots.cache.spec.dummy");
        assertThat(CaffeineSpecResolver.composeKey("largeShort")).isEqualTo("coffee-boots.cache.spec.largeShort");
        assertThat(CaffeineSpecResolver.composeKey("medium")).isEqualTo("coffee-boots.cache.spec.medium");
        assertThat(CaffeineSpecResolver.composeKey("tinyLong")).isEqualTo("coffee-boots.cache.spec.tinyLong");
    }
}
