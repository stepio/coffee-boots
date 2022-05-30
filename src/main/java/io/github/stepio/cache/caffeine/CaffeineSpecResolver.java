package io.github.stepio.cache.caffeine;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Igor Stepanov
 */
public class CaffeineSpecResolver implements EnvironmentAware {

    private static final String CACHE_BASIC_SPEC = "coffee-boots.cache.basic-spec";
    private static final String CACHE_SPEC_CUSTOM = "coffee-boots.cache.spec.%s";
    private static final String VALIDATION_MESSAGE
            = String.format("Failed to parse specified '%s' property", CACHE_BASIC_SPEC);

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getCaffeineSpec(String name) {
        String custom = this.environment.getProperty(composeKey(name));
        String basic = this.environment.getProperty(CACHE_BASIC_SPEC);
        if (StringUtils.hasText(basic)) {
            return mergeSpecs(basic, custom);
        }
        return custom;
    }

    protected static String composeKey(String name) {
        return String.format(CACHE_SPEC_CUSTOM, name);
    }

    protected static String mergeSpecs(String... specs) {
        Map<String, String> merged = new HashMap<>();
        for (String spec : specs) {
            if (spec != null) {
                merged.putAll(split(spec));
            }
        }
        return join(merged);
    }

    protected static Map<String, String> split(String value) {
        return Arrays.stream(value.split(","))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(CaffeineSpecResolver::key, CaffeineSpecResolver::value));
    }

    protected static String key(String[] pair) {
        Assert.state(pair.length > 0, VALIDATION_MESSAGE);
        return pair[0];
    }

    protected static String value(String[] pair) {
        if (pair.length > 1) {
            return pair[1];
        }
        return "";
    }

    protected static String join(Map<String, String> map) {
        return map.entrySet().stream()
                .map(CaffeineSpecResolver::join)
                .collect(Collectors.joining(","));
    }

    protected static String join(Map.Entry<String, String> entry) {
        if (StringUtils.hasText(entry.getValue())) {
            return entry.getKey() + "=" + entry.getValue();
        }
        return entry.getKey();
    }
}
