package io.github.stepio.cache.caffeine;

import io.github.stepio.cache.support.CachedDataHolder;
import io.github.stepio.cache.support.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class MultiConfigurationCacheManagerTest extends TestBase {

    @Autowired
    private CachedDataHolder cachedDataHolder;

    @Test
    public void testCreateNativeCaffeineCacheWithPreconfiguredCaches() {
        Object longTerm = this.cachedDataHolder.newCachedLongTermObject();
        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);

        Object aShortTerm = this.cachedDataHolder.newShortTermObject();
        Object bShortTerm = this.cachedDataHolder.newShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        final Object etalon = aShortTerm;
        await().atMost(2100, TimeUnit.MILLISECONDS)
                .until(() -> this.cachedDataHolder.newCachedShortTermObject() != etalon);

        bShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(bShortTerm).isNotSameAs(aShortTerm);
        aShortTerm = this.cachedDataHolder.newCachedShortTermObject();
        assertThat(aShortTerm).isSameAs(bShortTerm);

        assertThat(this.cachedDataHolder.newCachedLongTermObject()).isSameAs(longTerm);
    }
}
