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

    /** Marca o desmarca un checkbox solo si su estado actual es distinto al deseado. */
    protected void setCheckbox(By locator, boolean shouldBeChecked) {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (checkbox.isSelected() != shouldBeChecked) {
            checkbox.click();
        }
    }

    /**
     * Selecciona una opción por su texto visible en un <select>.
     * Si el select está reemplazado por Select2 (como País y Departamento en WooCommerce),
     * interactúa con el componente visible de Select2 en lugar del <select> oculto.
     * Espera a que la opción exista, así funciona con listas que cargan por AJAX (Ciudad).
     */
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
            // Si no desaparece, el siguiente paso fallará con un error más claro
        }
    }

    // ---------- Estados (devuelven true/false, nunca hacen assert) ----------

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

    // ---------- Navegación ----------

    public void goBack() {
        driver.navigate().back();
    }

    // ---------- Datos del navegador ----------

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}