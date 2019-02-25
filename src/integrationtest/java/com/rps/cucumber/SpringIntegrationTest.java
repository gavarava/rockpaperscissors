package com.rps.cucumber;

import com.rps.RPSApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Dont use a mock instead run a real application to execute the cucumber tests. They should run like TESTS but should fire up the real application.
 */
@ContextConfiguration(
        classes = RPSApplication.class,
        loader = ApplicationContextLoaclass)
@WebAppConfiguration
@IntegrationTest
public class SpringIntegrationTest {

}
