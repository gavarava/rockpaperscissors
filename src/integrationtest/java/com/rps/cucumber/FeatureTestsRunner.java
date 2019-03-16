package com.rps.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        tags = {"not @ignored"},
        features = "src/integrationtest/java/features",
        glue = "com.rps.cucumber.glue"
)
public class FeatureTestsRunner {
}
