package org.luis.tests;

import org.luis.pages.BasePage;
import org.luis.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected void skipIfBlocked(BasePage page) {
        if (page.isBlockedByServer()) {
            throw new SkipException("El servidor respondió 403 Forbidden: bloqueo por firewall, "
                    + "no un fallo del sitio. URL: " + page.getCurrentUrl());
        }
    }
}