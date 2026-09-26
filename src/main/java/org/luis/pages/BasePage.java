package org.luis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ---------- Interacciones ----------

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void clickFirstVisible(By locator) {
        WebElement element = wait.until(d -> d.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Actions(driver).moveToElement(element).perform();
    }

    protected void setCheckbox(By locator, boolean shouldBeChecked) {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (checkbox.isSelected() != shouldBeChecked) {
            checkbox.click();
        }
    }

    protected void selectOption(String selectId, String optionText) {
        By select2Container = By.id("select2-" + selectId + "-container");

        if (!driver.findElements(select2Container).isEmpty()) {
            click(select2Container);
            By searchField = By.cssSelector(".select2-container--open .select2-search__field");
            if (!driver.findElements(searchField).isEmpty()) {
                type(searchField, optionText);
            }
            click(By.xpath("//li[contains(@class,'select2-results__option') and normalize-space()='"
                    + optionText + "']"));
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//select[@id='" + selectId + "']/option[normalize-space()='" + optionText + "']")));
            new Select(driver.findElement(By.id(selectId))).selectByVisibleText(optionText);
        }
    }

    protected void waitForInvisibility(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException ignored) {
        }
    }


    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isNotDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isUrlContaining(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isBlockedByServer() {
        String title = driver.getTitle();
        return title.contains("403") || title.toLowerCase().contains("forbidden");
    }


    public void goBack() {
        driver.navigate().back();
    }


    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}