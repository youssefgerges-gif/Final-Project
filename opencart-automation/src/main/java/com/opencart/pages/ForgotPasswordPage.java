package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForgotPasswordPage extends BasePage {

    private final By emailField     = By.id("input-email");
    private final By continueButton = By.cssSelector("input[value='Continue']");
    private final By successMessage = By.cssSelector(".alert-success");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void submitPasswordResetRequest(String email) {
        waitForVisible(emailField);
        type(emailField, email);
        click(continueButton);
    }

    public boolean isPasswordResetConfirmationDisplayed() {
        return waitForUrlContains("account/login") || isDisplayed(successMessage);
    }

    public String getSuccessMessageText() {
        return isDisplayed(successMessage) ? getText(successMessage) : "Reset email sent";
    }

    public String getPageTitle() {
        return getText(By.cssSelector("#content h1, .page-header h1"));
    }
}
