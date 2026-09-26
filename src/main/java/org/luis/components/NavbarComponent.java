package org.luis.components;

import org.luis.pages.BasePage;
import org.luis.pages.CartPage;
import org.luis.pages.CategoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NavbarComponent extends BasePage {

    private final By cartLink = By.cssSelector("a[href*='/carrito/']");

    public NavbarComponent(WebDriver driver) {
        super(driver);
    }

    private By categoryLink(String slug) {
        return By.cssSelector("li.menu-item > a[href*='/categoria-producto/" + slug + "/']");
    }

    public CategoryPage goToCategory(String slug) {
        clickFirstVisible(categoryLink(slug));
        return new CategoryPage(driver);
    }

    public int getCartCount() {
        String digits = driver.findElements(cartLink).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .map(link -> link.getText().replaceAll("\\D", ""))
                .orElse("");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    public boolean waitForCartCount(int expected) {
        try {
            return wait.until(d -> getCartCount() >= expected);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public CartPage openCart() {
        clickFirstVisible(cartLink);
        return new CartPage(driver);
    }
}