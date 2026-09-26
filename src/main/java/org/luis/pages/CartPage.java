package org.luis.pages;

import org.luis.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector(".cart-contents.transition-all");
    private final By checkoutButton = By.xpath("//a[normalize-space()='Finalizar compra']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public static CartPage open(WebDriver driver) {
        driver.get(ConfigReader.get("baseUrl") + "/carrito/");
        return new CartPage(driver);
    }

    public int getItemCount() {
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(cartItems, 0));
        } catch (TimeoutException e) {
            return 0;
        }
        return driver.findElements(cartItems).size();
    }

    public boolean containsProduct(String name) {
        if (getItemCount() == 0) {
            return false;
        }
        return driver.findElements(cartItems).stream()
                .anyMatch(item -> item.getText().toLowerCase().contains(name.toLowerCase()));
    }

    public CheckoutPage goToCheckout() {
        clickFirstVisible(checkoutButton);
        return new CheckoutPage(driver);
    }
}