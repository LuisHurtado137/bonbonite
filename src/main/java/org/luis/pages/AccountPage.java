package org.luis.pages;

import org.luis.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class AccountPage extends BasePage {

    // Locators de Registro
    private final By regCedula = By.id("#reg_username");
    private final By regFirstName = By.id("#first_name");
    private final By regLastName = By.id("#last_name");
    private final By regEmail = By.id("#reg_email");
    private final By regPassword = By.id("#reg_password");
    private final By regPasswordConfirm = By.id("#reg_password2");
    private final By regPrivacyPolicy = By.id("#privacy_policy_reg");
    private final By registerButton = By.cssSelector("button[name='register']");


    // Locators de Login
    private final By loginCedula = By.id("#username");
    private final By loginPassword = By.id("#password");
    private final By loginButton = By.cssSelector("button[name='login']");

    // Mi cuenta locators
    private final By accountNavigation = By.id("#user-icon-wrap");
    WebElement accountMenu = driver.findElement(By.id("header-account-menu"));
    private final By editAccountLink = By.linkText("Datos");
    private final By successMessage = By.cssSelector(".woocommerce-message");
    private final By errorMessage = By.cssSelector(".woocommerce-error");

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public static AccountPage open(WebDriver driver) {
        driver.get(ConfigReader.get("baseUrl") + "/mi-cuenta/");
        return new AccountPage(driver);
    }

    // ---------- Acciones ----------

    public AccountPage register(String cedula, String nombres, String apellidos,
                                String email, String password) {
        type(regCedula, cedula);
        type(regFirstName, nombres);
        type(regLastName, apellidos);
        type(regEmail, email);
        type(regPassword, password);
        type(regPasswordConfirm, password);
        click(regPrivacyPolicy);
        click(registerButton);
        return this;
    }

    public AccountPage login(String email, String password) {
        type(loginCedula, email);
        type(loginPassword, password);
        click(loginButton);
        return this;
    }

    public EditAccountPage goToEditAccount() {
        new Actions(driver).moveToElement(accountMenu).perform();
        click(editAccountLink);
        return new EditAccountPage(driver);
    }

    // ---------- Estados ----------

    public boolean isLoggedIn() {
        return isDisplayed(accountNavigation);
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}