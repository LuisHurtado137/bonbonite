package org.luis.pages;

import org.luis.models.BillingInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By continueButton =
            By.xpath("//*[self::button or self::a][normalize-space()='Continuar']");

    private static final String DOCUMENT_TYPE_ID = "ID_TIPO_DOCUMENTO";
    private static final String COUNTRY_ID = "billing_country";
    private static final String STATE_ID = "billing_state";
    private static final String CITY_ID = "billing_city";

    private final By documentNumber = By.id("#billing_user_login");
    private final By firstName = By.id("#billing_first_name");
    private final By lastName = By.id("#billing_last_name");
    private final By email = By.id("#billing_email");
    private final By phone = By.id("#billing_phone");
    private final By address = By.id("#billing_address_1");

    private final By dataTreatmentCheckbox = By.id("#terms");

    private final By placeOrderButton = By.id("#place_order");

    private final By loadingOverlay = By.cssSelector(".blockUI.blockOverlay");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    private By genderOption(String gender) {
        return By.xpath("//label[normalize-space()='" + gender + "']");
    }


    public CheckoutPage clickContinue() {
        clickFirstVisible(continueButton);
        return this;
    }

    public CheckoutPage fillBilling(BillingInfo info) {
        selectOption(DOCUMENT_TYPE_ID, info.documentType());
        type(documentNumber, info.documentNumber());
        type(firstName, info.firstName());
        type(lastName, info.lastName());
        clickFirstVisible(genderOption(info.gender()));
        type(email, info.email());
        type(phone, info.phone());

        selectOption(COUNTRY_ID, info.country());
        waitForInvisibility(loadingOverlay);
        selectOption(STATE_ID, info.state());
        waitForInvisibility(loadingOverlay);
        selectOption(CITY_ID, info.city());
        waitForInvisibility(loadingOverlay);

        type(address, info.address());
        return this;
    }

    public CheckoutPage acceptDataTreatment() {
        setCheckbox(dataTreatmentCheckbox, true);
        return this;
    }

    public PaymentPage placeOrder() {
        waitForInvisibility(loadingOverlay);
        click(placeOrderButton);
        return new PaymentPage(driver);
    }


    public boolean isPlaceOrderButtonDisplayed() {
        waitForInvisibility(loadingOverlay);
        return isDisplayed(placeOrderButton);
    }

    public boolean isDataTreatmentAccepted() {
        return driver.findElement(dataTreatmentCheckbox).isSelected();
    }
}