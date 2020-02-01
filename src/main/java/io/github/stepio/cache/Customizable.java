package io.github.stepio.cache;

import java.util.List;

/**
 * Supports additional post-processing for newly created
 * {@link org.springframework.cache.Cache} instances.
 *
 * @author Igor Stepanov
 */
public interface Customizable {

    /**
     * Set list of {@link CacheCustomizer} instances to be invoked upon creation of each Cache instance.
     * @param customizers list of CacheCustomizer instances to be invoked
     * @see CacheCustomizer#onCreate(String, org.springframework.cache.Cache)
     */
    void setCustomizers(List<CacheCustomizer> customizers);
}
