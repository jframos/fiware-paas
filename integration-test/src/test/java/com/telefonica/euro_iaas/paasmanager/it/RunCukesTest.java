package com.telefonica.euro_iaas.paasmanager.it;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(

glue = { "classpath:com.telefonica.euro_iaas.paasmanager.it", "classpath:src/test/resource" }, features = "features/", format = {
    "pretty", "html:target/cucumber-html-report", "json-pretty:target/cucumber-report.json" }

)
public class RunCukesTest {
}
