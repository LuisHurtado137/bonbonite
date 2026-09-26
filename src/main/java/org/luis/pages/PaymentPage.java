package org.luis.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By wompiButton = By.xpath("//button[normalize-space()='Paga con Wompi']");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isWompiPaymentDisplayed() {
        return isDisplayed(wompiButton);
    }

}