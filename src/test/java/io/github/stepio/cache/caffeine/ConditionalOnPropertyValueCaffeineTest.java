package io.github.stepio.cache.caffeine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CaffeineSpecAutoConfiguration}.
 *
 * @author Igor Stepanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {ConditionalOnPropertyValueCaffeineTest.TestContext.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ConditionalOnPropertyValueCaffeineTest {

    @BeforeClass
    public static void setUp() {
        System.setProperty("spring.cache.type", "caffeine");
    }

    @AfterClass
    public static void tearDown() {
        System.clearProperty("spring.cache.type");
    }

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testConditionalOnProperty() {
        assertThat(cacheManager).isInstanceOf(MultiConfigurationCacheManager.class);
    }

    @EnableCaching
    @SpringBootApplication
    static class TestContext {
    }
}
