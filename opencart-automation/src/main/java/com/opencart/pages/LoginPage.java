package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By emailField         = By.id("input-email");
    private final By passwordField      = By.id("input-password");
    private final By loginButton        = By.cssSelector("input[value='Login']");
    private final By errorMessage       = By.cssSelector(".alert-danger");
    private final By forgotPasswordLink = By.linkText("Forgotten Password");
    private final By loggedInIndicator  = By.linkText("Logout");
    private final By myAccountLink      = By.xpath("//span[text()='My Account']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/login");
        waitForVisible(emailField);
    }

    public void loginWithCredentials(String email, String password) {
        type(emailField,    email);
        type(passwordField, password);
        click(loginButton);
    }

    public void navigateAndLogin(String email, String password) {
        navigateToLoginPage();
        loginWithCredentials(email, password);
    }

    public boolean isLoginSuccessful() {
        return waitForUrlContains("account/account") || isDisplayed(loggedInIndicator);
    }

    public String getLoginErrorText() {
        waitForVisible(errorMessage);
        return getText(errorMessage);
    }

    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    public String getLoggedInGreetingText() {
        return isDisplayed(loggedInIndicator) ? "Logged In" : "";
    }
}
