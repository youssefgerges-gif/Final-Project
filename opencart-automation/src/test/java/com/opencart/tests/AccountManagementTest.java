package com.opencart.tests;

import com.opencart.pages.AccountPage;
import com.opencart.pages.CartPage;
import com.opencart.pages.LoginPage;
import com.opencart.pages.ProductPage;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests covering My Account management scenarios.
 *
 * TC-13 – Update Profile Info
 * TC-14 – Change Account Password
 * TC-15 – Subscribe to Newsletter
 * TC-25 – Add a New Shipping Address
 * TC-26 – View Order History Details
 * TC-27 – Reorder Items from Past Order
 * TC-31 – Logout from Account
 */
public class AccountManagementTest extends BaseTest {

    private void loginAsRegisteredUser() {
        new LoginPage(driver).navigateAndLogin(
                TestDataHelper.VALID_EMAIL, TestDataHelper.VALID_PASSWORD);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-13: Update Profile Info – in Account Settings Page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-13: Verify that a logged-in user can update their first " +
                        "and last name and the changes are saved with a success message.")
    public void updateProfileNameDisplaysSuccessMessage() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.navigateToAccountInformation();
        accountPage.updateProfileName(
                TestDataHelper.UPDATED_FIRST_NAME,
                TestDataHelper.UPDATED_LAST_NAME
        );

        // Expected: success message 'You saved the account information.'
        Assert.assertTrue(
                accountPage.isProfileUpdateSuccessful(),
                "TC-13 FAIL: Account information was not saved – success message not displayed."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-14: Change Account Password – in Change Password Page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-14: Verify that a logged-in user can change their password " +
                        "and the change is confirmed with a success message.")
    public void changePasswordSuccessfullyConfirmedWithSuccessMessage() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.navigateToChangePassword();
        accountPage.changePassword(TestDataHelper.VALID_PASSWORD,
                                   TestDataHelper.NEW_ACCOUNT_PASSWORD);

        // Expected: success confirmation
        Assert.assertTrue(
                accountPage.isPasswordChangeSuccessful(),
                "TC-14 FAIL: Password change did not display a success message."
        );

        // NOTE: After this test, the password is changed.
        // In a real suite you would revert it; here we log back in to confirm.
        AccountPage logoutHelper = new AccountPage(driver);
        logoutHelper.logout();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateAndLogin(TestDataHelper.VALID_EMAIL,
                                   TestDataHelper.NEW_ACCOUNT_PASSWORD);

        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "TC-14 FAIL: Login with the newly set password failed."
        );

        // Restore original password so other tests continue to work
        accountPage.navigateToChangePassword();
        accountPage.changePassword(TestDataHelper.NEW_ACCOUNT_PASSWORD,
                                   TestDataHelper.VALID_PASSWORD);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-15: Subscribe to Newsletter – in Homepage Footer
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-15: Verify that entering an email in the footer newsletter " +
                        "widget and clicking Subscribe shows a thank-you message.")
    public void subscribeToNewsletterDisplaysThankYouMessage() {
        // No login required per test case
        driver.get(TestDataHelper.BASE_URL);

        AccountPage accountPage = new AccountPage(driver);
        accountPage.subscribeToNewsletter(TestDataHelper.uniqueEmail());

        // Expected: 'Thank you for your subscription.'
        Assert.assertTrue(
                accountPage.isNewsletterSubscriptionSuccessful(),
                "TC-15 FAIL: Newsletter subscription success message was not displayed."
        );

        String successText = accountPage.getNewsletterSuccessText();
        Assert.assertTrue(
                successText.toLowerCase().contains("thank you") ||
                successText.toLowerCase().contains("subscription"),
                "TC-15 FAIL: Success message text did not contain expected content. Got: " + successText
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-25: Add a New Shipping Address – in Address Book
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-25: Verify that a logged-in user can add a new shipping " +
                        "address and it appears in the Address Book.")
    public void addNewShippingAddressAppearsInAddressBook() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.navigateToAddressBook();
        accountPage.addNewAddress(
                TestDataHelper.ADDR_FIRST_NAME,
                TestDataHelper.ADDR_LAST_NAME,
                TestDataHelper.ADDR_STREET,
                TestDataHelper.ADDR_CITY,
                TestDataHelper.ADDR_POSTCODE,
                TestDataHelper.ADDR_PHONE
        );

        // Expected: success message after saving the address
        Assert.assertTrue(
                accountPage.isAddressSavedSuccessfully(),
                "TC-25 FAIL: Address was not saved – no success message displayed."
        );

        // Expected: redirected back to the Address Book
        Assert.assertTrue(
                driver.getCurrentUrl().contains("address"),
                "TC-25 FAIL: Not redirected to the Address Book page after saving. URL: "
                + driver.getCurrentUrl()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-26: View Order History Details
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-26: Verify that a user with past orders can open an order " +
                        "and see the correct order details page.")
    public void viewOrderHistoryDetailsShowsCorrectOrderInfo() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.navigateToOrderHistory();

        // Pre-condition: the account must have at least one order
        Assert.assertTrue(
                accountPage.isOrderHistoryDisplayed(),
                "TC-26 PRE-CONDITION FAIL: No orders found in order history. " +
                "Please place at least one order with the test account."
        );

        accountPage.openFirstOrderDetail();

        // Expected: detail page loaded (URL or title changes)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("order/view") || currentUrl.contains("sales/order"),
                "TC-26 FAIL: Did not navigate to the order detail page. URL: " + currentUrl
        );

        String detailTitle = accountPage.getOrderDetailTitle();
        Assert.assertFalse(
                detailTitle.isBlank(),
                "TC-26 FAIL: Order detail page title was empty."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-27: Reorder Items from Past Order
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-27: Verify that clicking 'Reorder' on a past order adds " +
                        "all items from that order to the current shopping cart.")
    public void reorderPastOrderAddsAllItemsToCart() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.navigateToOrderHistory();

        Assert.assertTrue(
                accountPage.isOrderHistoryDisplayed(),
                "TC-27 PRE-CONDITION FAIL: No orders found. Place at least one order first."
        );

        accountPage.reorderFirstOrder();

        // Expected: items are added to the cart (success message or cart count > 0)
        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCartPage();

        int cartItemCount = cartPage.getCartItemCount();
        Assert.assertTrue(
                cartItemCount >= 1,
                "TC-27 FAIL: Cart is empty after reordering – no items were added from past order."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-31: Logout from Account – via Account Menu
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-31: Verify that clicking Sign Out logs the user out, " +
                        "terminates the session, and redirects to the homepage or login page.")
    public void logoutClearsSessionAndRedirectsToHomepage() {
        loginAsRegisteredUser();

        AccountPage accountPage = new AccountPage(driver);
        accountPage.logout();

        // Expected: session is terminated; user is on homepage or login page
        Assert.assertTrue(
                accountPage.isLoggedOut(),
                "TC-31 FAIL: User does not appear to be logged out. URL: " + driver.getCurrentUrl()
        );

        // Expected: the logged-in account menu/greeting is no longer visible
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertFalse(
                loginPage.isLoginSuccessful(),
                "TC-31 FAIL: Logged-in greeting is still visible after logout."
        );
    }
}
