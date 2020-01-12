package io.github.stepio.cache;

import org.springframework.cache.Cache;

public interface CacheCustomizer {
    void onCreate(Cache cache);
}
