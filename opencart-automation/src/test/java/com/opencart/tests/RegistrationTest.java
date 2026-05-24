package com.opencart.tests;

import com.opencart.pages.RegistrationPage;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests covering user-account registration scenarios.
 *
 * TC-01 – Register a New Account
 * TC-02 – Register with Already Used Email
 */
public class RegistrationTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-01: Register a New Account – on Registration Page – when user is not
    //        logged in
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-01: Verify that a new user can register successfully and " +
                        "is redirected to the account dashboard with a welcome message.")
    public void registerNewAccountSuccessfully() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.navigateToRegistrationPage();

        registrationPage.registerNewAccount(
                TestDataHelper.NEW_FIRST_NAME,
                TestDataHelper.NEW_LAST_NAME,
                TestDataHelper.uniqueEmail(),      // unique to avoid TC-02 conflicts
                TestDataHelper.REGISTRATION_PASSWORD
        );

        // Expected: account is created and user is redirected to the account dashboard
        Assert.assertTrue(
                registrationPage.isRedirectedToAccountDashboard(),
                "TC-01 FAIL: User was not redirected to the account dashboard after registration."
        );

        Assert.assertTrue(
                registrationPage.isRegistrationSuccessful(),
                "TC-01 FAIL: Success message was not displayed after registration."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-02: Register with Already Used Email – on Registration Page – when user
    //        submits duplicate email
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-02: Verify that registering with an already-used email " +
                        "displays an appropriate error message.")
    public void registerWithDuplicateEmailShowsError() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.navigateToRegistrationPage();

        // Use the known, pre-existing account e-mail
        registrationPage.registerNewAccount(
                TestDataHelper.NEW_FIRST_NAME,
                TestDataHelper.NEW_LAST_NAME,
                TestDataHelper.VALID_EMAIL,         // already registered e-mail
                TestDataHelper.REGISTRATION_PASSWORD
        );

        // Expected: error message indicates the account already exists
        String errorText = registrationPage.getDuplicateEmailErrorText();
        Assert.assertTrue(
                errorText.toLowerCase().contains("already") ||
                errorText.toLowerCase().contains("account with this email"),
                "TC-02 FAIL: Expected a duplicate-email error message, but got: " + errorText
        );
    }
}
