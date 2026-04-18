
package com.clarkewerton.driver;

import com.clarkewerton.driver.manager.AndroidDriverManager;
import com.clarkewerton.driver.manager.IOSDriverManager;
import com.clarkewerton.exception.PlatformNotSupportedException;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class DriverFactory {

    public AppiumDriver createInstance(String platform, String udid, String platformVersion, String env) {
        AppiumDriver driver;
        Platform mobilePlatform = Platform.valueOf(platform.toUpperCase());

        switch (mobilePlatform) {
            case IOS:
                driver = new IOSDriverManager().createInstance(udid, platformVersion, env);
                break;

            case ANDROID:
                driver = new AndroidDriverManager().createInstance(udid, platformVersion, env);
                break;

            default:
                throw new PlatformNotSupportedException(
                    "Platform not supported! Check if you set ios or android on the parameter.");
        }
        return driver;
    }
}
