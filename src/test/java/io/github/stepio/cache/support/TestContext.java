package io.github.stepio.cache.support;

import org.junit.Ignore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * Test context for testing the whole Spring context with auto-configured beans.
 *
 * @author Igor Stepanov
 */
@Ignore
@SpringBootApplication
@EnableCaching
public class TestContext {

    @Bean
    public CachedDataHolder cachedDataHolder() {
        return new CachedDataHolder();
    }
}
