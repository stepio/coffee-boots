package io.github.stepio.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

/**
 * Supplier for {@link Caffeine} instances, constructed from {@link com.github.benmanes.caffeine.cache.CaffeineSpec} values.
 * Allows configuring different expiration, capacity and other caching parameters for different cache names.
 *
 * @author Igor Stepanov
 */
public class CaffeineSupplier implements Function<String, Caffeine<Object, Object>>, EnvironmentAware {

    private static final String CACHE_SPEC_CUSTOM = "coffee-boots.cache.spec.%s";

    protected Environment environment;
    protected final ConcurrentMap<String, Caffeine<Object, Object>> cacheBuilders = new ConcurrentHashMap<>(16);

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void putCaffeine(String name, Caffeine<Object, Object> caffeine) {
        notNull(caffeine, "Non-null Caffeine is mandatory");
        this.cacheBuilders.put(name, caffeine);
    }

    public void putCaffeineSpec(String name, CaffeineSpec caffeineSpec) {
        notNull(caffeineSpec, "Non-null Caffeine specification is mandatory");
        putCaffeine(name, Caffeine.from(caffeineSpec));
    }

    public void putCaffeineSpecification(String name, String caffeineSpec) {
        hasText(caffeineSpec, "Non-empty Caffeine specification is mandatory");
        putCaffeineSpec(name, CaffeineSpec.parse(caffeineSpec));
    }

    @Override
    public Caffeine<Object, Object> apply(String name) {
        String value = this.environment.getProperty(composeKey(name));
        if (StringUtils.hasText(value)) {
            return Caffeine.from(value);
        }
        return cacheBuilders.get(name);
    }

    protected String composeKey(String name) {
        return String.format(CACHE_SPEC_CUSTOM, name);
    }
}
