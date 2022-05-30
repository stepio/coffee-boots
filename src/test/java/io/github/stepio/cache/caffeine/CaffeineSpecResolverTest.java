package io.github.stepio.cache.caffeine;

import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CaffeineSpecResolverTest {

    private static final String CACHE_BASIC_SPEC = "coffee-boots.cache.basic-spec";

    private Environment environment;
    private CaffeineSpecResolver caffeineSpecResolver;

    @BeforeEach
    public void setUp() {
        this.environment = mock(Environment.class);

        doReturn("maximumSize=100")
                .when(this.environment)
                .getProperty(eq(CACHE_BASIC_SPEC));

        doReturn("expireAfterWrite=7m")
                .when(this.environment)
                .getProperty(eq("coffee-boots.cache.spec.reduced"));

        doReturn("maximumSize=5000,expireAfterWrite=12h")
                .when(this.environment)
                .getProperty(eq("coffee-boots.cache.spec.full"));

        doReturn("expireAfterWrite=20s,recordStats")
                .when(this.environment)
                .getProperty(eq("coffee-boots.cache.spec.exotic"));

        this.caffeineSpecResolver = new CaffeineSpecResolver();
        this.caffeineSpecResolver.setEnvironment(this.environment);
    }

    @Test
    public void testGetCaffeineSpecDefined() {
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("reduced"))
                .contains("maximumSize=100", "expireAfterWrite=7m", ",");
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("full"))
                .contains("maximumSize=5000", "expireAfterWrite=12h", ",");
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("exotic"))
                .contains("maximumSize=100", "expireAfterWrite=20s", "recordStats", ",")
                .doesNotContain("recordStats=");
    }

    @Test
    public void testGetCaffeineSpecNotDefined() {
        doReturn(null)
                .when(this.environment)
                .getProperty(eq(CACHE_BASIC_SPEC));

        assertThat(this.caffeineSpecResolver.getCaffeineSpec("reduced"))
                .contains("expireAfterWrite=7m");
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("full"))
                .contains("maximumSize=5000", "expireAfterWrite=12h", ",");

        doReturn("")
                .when(this.environment)
                .getProperty(eq(CACHE_BASIC_SPEC));

        assertThat(this.caffeineSpecResolver.getCaffeineSpec("reduced"))
                .contains("expireAfterWrite=7m");
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("full"))
                .contains("maximumSize=5000", "expireAfterWrite=12h", ",");
    }

    @Test
    public void testGetCaffeineSpecBasicForCacheWithoutCustomSpec() {
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("unspeced"))
                .isEqualTo("maximumSize=100");
        doReturn(null)
                .when(this.environment)
                .getProperty(eq(CACHE_BASIC_SPEC));
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("unspeced"))
                .isNull();
    }

    @Test
    public void testGetCaffeineSpecMalformed() {
        doReturn("dummy")
                .when(this.environment)
                .getProperty(eq(CACHE_BASIC_SPEC));

        assertThat(this.caffeineSpecResolver.getCaffeineSpec("reduced"))
                .contains("expireAfterWrite=7m", "dummy", ",")
                .doesNotContain("dummy=");
        assertThat(this.caffeineSpecResolver.getCaffeineSpec("full"))
                .contains("maximumSize=5000", "expireAfterWrite=12h", "dummy", ",")
                .doesNotContain("dummy=");
    }

    @Test
    public void testComposeKeyWithDifferentCacheNames() {
        assertThat(CaffeineSpecResolver.composeKey("dummy")).isEqualTo("coffee-boots.cache.spec.dummy");
        assertThat(CaffeineSpecResolver.composeKey("largeShort")).isEqualTo("coffee-boots.cache.spec.largeShort");
        assertThat(CaffeineSpecResolver.composeKey("medium")).isEqualTo("coffee-boots.cache.spec.medium");
        assertThat(CaffeineSpecResolver.composeKey("tinyLong")).isEqualTo("coffee-boots.cache.spec.tinyLong");
    }

    @Test
    public void testMergeSpecsValid() {
        assertThat(CaffeineSpecResolver.mergeSpecs("maximumSize=5000"))
                .isEqualTo("maximumSize=5000");
        assertThat(CaffeineSpecResolver.mergeSpecs("maximumSize=5000,expireAfterWrite=5m"))
                .isEqualTo("maximumSize=5000,expireAfterWrite=5m");
        assertThat(CaffeineSpecResolver.mergeSpecs("maximumSize=5000", "expireAfterWrite=5m"))
                .isEqualTo("maximumSize=5000,expireAfterWrite=5m");
        assertThat(CaffeineSpecResolver.mergeSpecs("maximumSize=5000", "maximumSize=1000,expireAfterWrite=5m"))
                .isEqualTo("maximumSize=1000,expireAfterWrite=5m");
    }

    @Test
    public void testMergeSpecsInvalid() {
        assertThatThrownBy(() -> CaffeineSpecResolver.mergeSpecs(null))
                .isInstanceOf(NullPointerException.class);
        assertThat(CaffeineSpecResolver.mergeSpecs(""))
                .isEqualTo("");
        assertThat(CaffeineSpecResolver.mergeSpecs("dummy"))
                .isEqualTo("dummy");
    }

    @Test
    public void testSplitValid() {
        assertThat(CaffeineSpecResolver.split("maximumSize=5000"))
                .containsOnly(entry("maximumSize", "5000"));
        assertThat(CaffeineSpecResolver.split("maximumSize=5000,expireAfterWrite=5m"))
                .containsOnly(entry("maximumSize", "5000"), entry("expireAfterWrite", "5m"));
    }

    @Test
    public void testSplitInvalid() {
        assertThatThrownBy(() -> CaffeineSpecResolver.split(null))
                .isInstanceOf(NullPointerException.class);
        assertThat(CaffeineSpecResolver.split(""))
                .containsOnly(entry("", ""));
        assertThat(CaffeineSpecResolver.split("dummy"))
                .containsOnly(entry("dummy", ""));
    }

    @Test
    public void testJoinValid() {
        assertThat(CaffeineSpecResolver.join(Collections.singletonMap("maximumSize", "5000")))
                .isEqualTo("maximumSize=5000");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("maximumSize", "5000");
        map.put("expireAfterWrite", "5m");
        assertThat(CaffeineSpecResolver.join(map))
                .isEqualTo("maximumSize=5000,expireAfterWrite=5m");
    }

    @Test
    public void testJoinInvalid() {
        assertThatThrownBy(() -> CaffeineSpecResolver.join((Map) null))
                .isInstanceOf(NullPointerException.class);
        assertThat(CaffeineSpecResolver.join(Collections.emptyMap())).isEmpty();
    }
}
