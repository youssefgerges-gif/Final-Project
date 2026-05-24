package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage extends BasePage {

    private final By firstNameField   = By.id("input-firstname");
    private final By lastNameField    = By.id("input-lastname");
    private final By emailField       = By.id("input-email");
    private final By passwordField    = By.id("input-password");
    private final By confirmPassField = By.id("input-confirm");
    private final By agreeCheckbox    = By.name("agree");
    private final By submitButton     = By.cssSelector("input[value='Continue']");
    private final By errorMessage     = By.cssSelector(".alert-danger, .text-danger");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToRegistrationPage() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/register");
        waitForVisible(firstNameField);
    }

    public void registerNewAccount(String firstName, String lastName,
                                   String email, String password) {
        type(firstNameField,   firstName);
        type(lastNameField,    lastName);
        type(emailField,       email);
        type(passwordField,    password);
        type(confirmPassField, password);
        if (!driver.findElement(agreeCheckbox).isSelected()) {
            click(agreeCheckbox);
        }
        click(submitButton);
    }

    public boolean isRegistrationSuccessful() {
        return waitForUrlContains("account/success");
    }

    public String getDuplicateEmailErrorText() {
        waitForVisible(errorMessage);
        return getText(errorMessage);
    }

    public boolean isRedirectedToAccountDashboard() {
        return waitForUrlContains("account/success");
    }
}
