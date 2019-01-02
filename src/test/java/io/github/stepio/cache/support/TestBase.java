package io.github.stepio.cache.support;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test basis for testing the whole Spring context with auto-configured beans.
 *
 * @author Igor Stepanov
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestContext.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestBase {
}
