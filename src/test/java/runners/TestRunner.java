package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = {"hooks", "stepdefinitions"},
    plugin    = {
        "pretty",
        "html:reports/cucumber-report.html",
        "json:reports/cucumber-report.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {}
