package org.luis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ProductPage extends BasePage {

    private final By productName = By.cssSelector("div.right-column > div.sticky > h1.product_title");                                // TODO: confirmar
    private final By variationSelects = By.cssSelector("div.sticky > div.color_select > ul.bonbonite_colors");
    private final By addToCartButton = By.cssSelector("button[type='submit']");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName).trim();
    }

    public ProductPage selectRequiredOptions() {
        int count = driver.findElements(variationSelects).size();

        for (int i = 0; i < count; i++) {
            WebElement selectElement = driver.findElements(variationSelects).get(i);
            Select select = new Select(selectElement);

            String current = select.getFirstSelectedOption().getDomAttribute("value");
            if (current != null && !current.isEmpty()) {
                continue;
            }

            select.getOptions().stream()
                    .filter(WebElement::isEnabled)
                    .map(option -> option.getDomAttribute("value"))
                    .filter(value -> value != null && !value.isEmpty())
                    .findFirst()
                    .ifPresent(select::selectByValue);
        }
        return this;
    }

    public ProductPage addToCart() {
        wait.until(d -> {
            String classes = d.findElement(addToCartButton).getDomAttribute("class");
            return classes != null && !classes.contains("disabled");
        });
        click(addToCartButton);
        return this;
    }
}