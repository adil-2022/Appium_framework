
package com.clarkewerton.test;

import com.clarkewerton.driver.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class BaseTest {

    protected AppiumDriver driver;
    private boolean isSauceLabsExecution;
    private String deviceId;

    @BeforeClass(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion", "env"})
    public void preCondition(String platform, String udid, String platformVersion, String env) throws IOException {
        driver = new DriverFactory().createInstance(platform, udid, platformVersion, env);
        isSauceLabsExecution = env != null && env.equals("cloud");
        this.deviceId = udid;
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachTest(ITestResult result) throws IOException, InterruptedException {
        reportTestStatus(result);
    }

    @AfterClass(alwaysRun = true)
    public synchronized void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void reportTestStatus(ITestResult result) {
        if (driver != null && !result.isSuccess()) {
            captureScreenshot(result.getName());
        }

        if (driver != null && isSauceLabsExecution) {
            String status = result.getStatus() == ITestResult.SUCCESS ? "passed" : "failed";
            try {
                driver.executeScript("sauce:job-result=" + status);
            } catch (Exception e) {
                System.err.println("Failed to update Sauce Labs status: " + e.getMessage());
            }
        }
    }

    private void captureScreenshot(String testName) {
        try {
            if (driver != null) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                byte[] fileContent = FileUtils.readFileToByteArray(screenshot);
                Allure.addAttachment("Screenshot - " + testName, new ByteArrayInputStream(fileContent));
            }
        } catch (IOException e) {
            System.err.println("Erro ao capturar screenshot: " + e.getMessage());
        }
    }

    protected AppiumDriver getDriver() {
        return driver;
    }
}

