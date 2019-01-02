package io.github.stepio.cache.caffeine;

import io.github.stepio.cache.support.CachedDataHolder;
import io.github.stepio.cache.support.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiConfigurationCacheManagerTest extends TestBase {

    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Test
    public void createNativeCaffeineCache_withCaches() {
        Object longTerm = this.cachedDataHolder.newCachedLongTermObject();
        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);

        Object aShortTerm = this.cachedDataHolder.newShortTermObject();
        Object bShortTerm = this.cachedDataHolder.newShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        sleep(2100L);
        bShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);
    }
}
