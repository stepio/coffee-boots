package io.github.stepio.cache.caffeine;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
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

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getCaffeineSpec(String name) {
        String custom = this.environment.getProperty(composeKey(name));
        if (StringUtils.isEmpty(custom)) {
            return null;
        }
        String basic = this.environment.getProperty(CACHE_BASIC_SPEC);
        if (StringUtils.hasText(basic)) {
            return mergeSpecs(basic, custom);
        }
        return custom;
    }

    static String composeKey(String name) {
        return String.format(CACHE_SPEC_CUSTOM, name);
    }

    static String mergeSpecs(String... specs) {
        Map<String, String> merged = new HashMap<>();
        for (String spec : specs) {
            merged.putAll(split(spec));
        }
        return join(merged);
    }

    static Map<String, String> split(String value) {
        return Arrays.stream(value.split(","))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
    }

    static String join(Map<String, String> map) {
        return map.entrySet().stream()
                .map(CaffeineSpecResolver::join)
                .collect(Collectors.joining(","));
    }

    static String join(Map.Entry<String, String> entry) {
        return entry.getKey() + "=" + entry.getValue();
    }
}
