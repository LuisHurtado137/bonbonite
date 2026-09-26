package org.luis.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        String browser = ConfigReader.get("browser");
        if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());
        } else {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.parseBoolean(ConfigReader.get("headless"))) {
                options.addArguments("--headless=new");
            }
            driver.set(new ChromeDriver(options));
        }
        driver.get().manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}