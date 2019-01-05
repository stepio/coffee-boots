package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.function.Function;

/**
 * Supplier for {@link Caffeine} instances, constructed from {@link com.github.benmanes.caffeine.cache.CaffeineSpec} values.
 * Allows configuring different expiration, capacity and other caching parameters for different cache names.
 *
 * @author Igor Stepanov
 */
public class CaffeineSupplier implements Function<String, Caffeine<Object, Object>>, EnvironmentAware {

    private static final String CACHE_SPEC_CUSTOM = "spring.cache.caffeine.spec.%s";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Caffeine<Object, Object> apply(String name) {
        String value = this.environment.getProperty(composeKey(name));
        if (StringUtils.hasText(value)) {
            return Caffeine.from(value);
        }
        return null;
    }

    protected String composeKey(String name) {
        return String.format(CACHE_SPEC_CUSTOM, name);
    }
}
