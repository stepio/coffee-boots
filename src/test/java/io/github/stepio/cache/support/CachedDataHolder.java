package io.github.stepio.cache.support;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class CachedDataHolder {

    @Cacheable("tinyLong")
    public Object newCachedLongTermObject() {
        return new Object();
    }

    @Cacheable("largeShort")
    public Object newCachedShortTermObject() {
        return new Object();
    }

    @CachePut("largeShort")
    public Object newShortTermObject() {
        return new Object();
    }
}
