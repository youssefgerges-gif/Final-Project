package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AccountPage extends BasePage {

    private final By myAccountMenu      = By.cssSelector("a[title='My Account']");
    private final By logoutLink         = By.linkText("Logout");
    private final By editAccountLink    = By.linkText("Edit Account");
    private final By changePasswordLink = By.linkText("Password");
    private final By addressBookLink    = By.linkText("Address Book");
    private final By orderHistoryLink   = By.linkText("Order History");
    private final By wishlistLink       = By.linkText("Wish List");

    // Edit Account
    private final By firstNameField     = By.id("input-firstname");
    private final By lastNameField      = By.id("input-lastname");
    private final By saveButton         = By.cssSelector("input[value='Continue']");
    private final By successMessage     = By.cssSelector(".alert-success");

    // Change Password
    private final By passwordField      = By.id("input-password");
    private final By confirmPassField   = By.id("input-confirm");

    // Newsletter
    private final By newsletterEmail    = By.cssSelector("input[name='email']");
    private final By subscribeBtn       = By.cssSelector("input[value='Subscribe']");

    // Address
    private final By addAddressBtn      = By.cssSelector(".btn-primary[href*='address/add']");
    private final By addrFirstName      = By.id("input-firstname");
    private final By addrLastName       = By.id("input-lastname");
    private final By addrCompany        = By.id("input-company");
    private final By addrStreet1        = By.id("input-address-1");
    private final By addrCity           = By.id("input-city");
    private final By addrPostcode       = By.id("input-postcode");
    private final By addrCountry        = By.id("input-country");
    private final By addrSaveBtn        = By.cssSelector("input[value='Continue']");

    // Orders
    private final By orderRows          = By.cssSelector("#content .table tbody tr");
    private final By viewOrderLink      = By.cssSelector(".btn[href*='order/info']");
    private final By reorderBtn         = By.cssSelector(".btn[href*='order/reorder']");

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public void logout() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/logout");
        waitForUrlContains("account/logout");
    }

    public boolean isLoggedOut() {
        return driver.getCurrentUrl().contains("account/logout") ||
               driver.getCurrentUrl().contains("account/login") ||
               isDisplayed(By.linkText("Login"));
    }

    public void navigateToAccountInformation() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/edit");
        waitForVisible(firstNameField);
    }

    public void updateProfileName(String newFirst, String newLast) {
        type(firstNameField, newFirst);
        type(lastNameField,  newLast);
        click(saveButton);
    }

    public boolean isProfileUpdateSuccessful() {
        waitForVisible(successMessage);
        return isDisplayed(successMessage);
    }

    public void navigateToChangePassword() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/password");
        waitForVisible(passwordField);
    }

    public void changePassword(String currentPassword, String newPassword) {
        type(passwordField,    newPassword);
        type(confirmPassField, newPassword);
        click(saveButton);
    }

    public boolean isPasswordChangeSuccessful() {
        waitForVisible(successMessage);
        return isDisplayed(successMessage);
    }

    public void subscribeToNewsletter(String email) {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/newsletter");
        By yesRadio = By.cssSelector("input[name='newsletter'][value='1']");
        waitForVisible(yesRadio);
        click(yesRadio);
        click(saveButton);
    }

    public boolean isNewsletterSubscriptionSuccessful() {
        waitForVisible(successMessage);
        return isDisplayed(successMessage);
    }

    public String getNewsletterSuccessText() {
        return getText(successMessage);
    }

    public void navigateToAddressBook() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/address");
        waitForVisible(By.cssSelector("#content"));
    }

    public void addNewAddress(String firstName, String lastName,
                               String street, String city,
                               String postcode, String phone) {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/address/add");
        waitForVisible(addrFirstName);
        type(addrFirstName, firstName);
        type(addrLastName,  lastName);
        type(addrStreet1,   street);
        type(addrCity,      city);
        type(addrPostcode,  postcode);
        try { selectByVisibleText(addrCountry, "United Kingdom"); } catch (Exception ignored) {}
        click(addrSaveBtn);
    }

    public boolean isAddressSavedSuccessfully() {
        waitForVisible(successMessage);
        return isDisplayed(successMessage);
    }

    public void navigateToOrderHistory() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/order");
        waitForVisible(By.cssSelector("#content"));
    }

    public void openFirstOrderDetail() {
        List<WebElement> viewLinks = waitForAllVisible(viewOrderLink);
        viewLinks.get(0).click();
    }

    public String getOrderDetailTitle() {
        By heading = By.cssSelector("#content h1");
        return isDisplayed(heading) ? getText(heading) : driver.getTitle();
    }

    public boolean isOrderHistoryDisplayed() {
        return driver.findElements(orderRows).size() > 0;
    }

    public void reorderFirstOrder() {
        List<WebElement> reorderLinks = waitForAllVisible(reorderBtn);
        reorderLinks.get(0).click();
        waitForVisible(By.cssSelector(".alert-success, #content"));
    }

    public void navigateToWishlist() {
        driver.get("https://awesomeqa.com/ui/index.php?route=account/wishlist");
    }

    public boolean isWishlistPageLoaded() {
        return waitForUrlContains("account/wishlist");
    }
}
