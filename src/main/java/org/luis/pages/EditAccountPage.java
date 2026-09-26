package org.luis.pages;

import org.luis.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EditAccountPage extends BasePage {

    private final By firstName = By.id("account_first_name");
    private final By lastName = By.id("account_last_name");
    private final By displayName = By.id("account_display_name");
    private final By saveButton = By.cssSelector("button[name='save_account_details']");

    public EditAccountPage(WebDriver driver) {
        super(driver);
    }

    public static EditAccountPage open(WebDriver driver) {
        driver.get(ConfigReader.get("baseUrl") + "/mi-cuenta/editar-cuenta/");
        return new EditAccountPage(driver);
    }

    public AccountPage updateName(String first, String last) {
        type(firstName, first);
        type(lastName, last);
        type(displayName, first);
        click(saveButton);
        return new AccountPage(driver);
    }

    public String getFirstName() {
        return getValue(firstName);
    }

    public String getLastName() {
        return getValue(lastName);
    }

    private String getValue(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getDomProperty("value");
    }
}