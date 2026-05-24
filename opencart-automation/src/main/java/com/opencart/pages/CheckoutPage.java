package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    // Step 1 - Account / Guest
    private final By guestRadio        = By.cssSelector("input[value='guest']");
    private final By continueGuestBtn  = By.id("button-account");

    // Step 2 - Billing Details
    private final By firstNameField    = By.id("input-payment-firstname");
    private final By lastNameField     = By.id("input-payment-lastname");
    private final By emailField        = By.id("input-payment-email");
    private final By phoneField        = By.id("input-payment-telephone");
    private final By address1Field     = By.id("input-payment-address-1");
    private final By cityField         = By.id("input-payment-city");
    private final By postcodeField     = By.id("input-payment-postcode");
    private final By countryDropdown   = By.id("input-payment-country");
    private final By continuePayBtn    = By.id("button-guest");

    // Step 3 - Delivery
    private final By continueDelivery  = By.id("button-shipping-method");

    // Step 4 - Payment
    private final By agreeCheckbox     = By.name("agree");
    private final By confirmOrderBtn   = By.id("button-confirm");

    // Confirmation
    private final By successHeading    = By.cssSelector("#common-success h1, #content h1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void continueAsGuest() {
        waitForVisible(guestRadio);
        click(guestRadio);
        click(continueGuestBtn);
        waitForVisible(firstNameField);
    }

    public void fillShippingDetails(String email, String firstName, String lastName,
                                    String street, String city, String postcode, String phone) {
        waitForVisible(firstNameField);
        type(firstNameField, firstName);
        type(lastNameField,  lastName);
        type(emailField,     email);
        type(phoneField,     phone);
        type(address1Field,  street);
        type(cityField,      city);
        type(postcodeField,  postcode);
        try { selectByVisibleText(countryDropdown, "United Kingdom"); } catch (Exception ignored) {}
        click(continuePayBtn);
    }

    public void selectPaymentAndPlaceOrder() {
        // Continue through delivery method step
        try {
            waitForClickable(continueDelivery).click();
        } catch (Exception ignored) {}

        // Agree to terms
        try {
            if (!driver.findElement(agreeCheckbox).isSelected()) {
                click(agreeCheckbox);
            }
        } catch (Exception ignored) {}

        waitForClickable(confirmOrderBtn).click();
    }

    public boolean isOrderConfirmed() {
        return waitForUrlContains("checkout/success");
    }

    public String getOrderNumber() {
        return isDisplayed(successHeading) ? getText(successHeading) : "Order confirmed";
    }

    public String getConfirmationTitle() {
        return isDisplayed(successHeading) ? getText(successHeading) : driver.getTitle();
    }
}
