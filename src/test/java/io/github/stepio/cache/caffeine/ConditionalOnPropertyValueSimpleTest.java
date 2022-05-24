package io.github.stepio.cache.caffeine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CaffeineSpecAutoConfiguration}.
 *
 * @author Igor Stepanov
 */
@SpringBootTest(
        classes = {ConditionalOnPropertyValueSimpleTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ConditionalOnPropertyValueSimpleTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testConditionalOnProperty() {
        assertThat(cacheManager).isNotInstanceOf(MultiConfigurationCacheManager.class);
    }

    @BeforeAll
    public static void setUp() {
        System.setProperty("spring.cache.type", "simple");
    }

    @AfterAll
    public static void tearDown() {
        System.clearProperty("spring.cache.type");
    }

    @EnableCaching
    @SpringBootApplication
    static class TestContext {
    }
}
