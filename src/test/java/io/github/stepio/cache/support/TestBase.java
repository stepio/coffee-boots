package io.github.stepio.cache.support;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test basis for testing the whole Spring context with auto-configured beans.
 *
 * @author Igor Stepanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestBase {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            LOG.error("Waiting failed", ex);
            Thread.currentThread().interrupt();
        }
    }
}
