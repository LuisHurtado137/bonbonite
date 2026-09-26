package org.luis.pages;

import org.luis.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By newArrivalsTitle = By.xpath("//h2[normalize-space()='Descubre todo lo nuevo']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /** Abre el home directamente por URL. */
    public static HomePage open(WebDriver driver) {
        driver.get(ConfigReader.get("baseUrl"));
        return new HomePage(driver);
    }

    public boolean isNewArrivalsSectionDisplayed() {
        return isDisplayed(newArrivalsTitle);
    }
}
