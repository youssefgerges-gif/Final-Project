package com.opencart.tests;

import com.opencart.pages.ForgotPasswordPage;
import com.opencart.pages.LoginPage;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests covering login and forgotten-password scenarios.
 *
 * TC-03 – Login with Valid Credentials
 * TC-04 – Login with Invalid Password
 * TC-05 – Reset Forgotten Password
 */
public class LoginTest extends BaseTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TC-03: Login with Valid Credentials – on Login Page – when user has a
    //        registered account
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-03: Verify that a registered user can log in with valid " +
                        "credentials and is redirected to the account dashboard.")
    public void loginWithValidCredentialsRedirectsToDashboard() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.navigateAndLogin(TestDataHelper.VALID_EMAIL, TestDataHelper.VALID_PASSWORD);

        // Expected: user is logged in and the account dashboard is shown
        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "TC-03 FAIL: User was not redirected to the dashboard after successful login."
        );

        String greetingText = loginPage.getLoggedInGreetingText();
        Assert.assertFalse(
                greetingText.isBlank(),
                "TC-03 FAIL: Logged-in greeting text was not visible in the top navigation."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-04: Login with Invalid Password – on Login Page – when user enters wrong
    //        credentials
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-04: Verify that logging in with an incorrect password " +
                        "displays the correct error message and does not authenticate the user.")
    public void loginWithInvalidPasswordDisplaysErrorMessage() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.navigateAndLogin(TestDataHelper.VALID_EMAIL, "WrongPassword999!");

        // Expected error text per the test case
        String errorText = loginPage.getLoginErrorText();
        Assert.assertTrue(
                errorText.toLowerCase().contains("incorrect") ||
                errorText.toLowerCase().contains("sign-in was incorrect") ||
                errorText.toLowerCase().contains("disabled"),
                "TC-04 FAIL: Expected an authentication-error message, but got: " + errorText
        );

        // User must remain on the login page (not authenticated)
        Assert.assertFalse(
                loginPage.isLoginSuccessful(),
                "TC-04 FAIL: User appears to be logged in despite an invalid password."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-05: Reset Forgotten Password – on Forgot Password Page – when user
    //        cannot remember password
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-05: Verify that submitting the Forgot Password form with " +
                        "a registered email displays a password-reset confirmation message.")
    public void forgotPasswordSubmissionDisplaysConfirmationMessage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);

        // Expected: a reset confirmation message is shown
        // (actual email delivery and new-password flow are outside scope of UI automation)
        forgotPasswordPage.submitPasswordResetRequest(TestDataHelper.VALID_EMAIL);

        Assert.assertTrue(
                forgotPasswordPage.isPasswordResetConfirmationDisplayed(),
                "TC-05 FAIL: Password reset confirmation message was not displayed."
        );

        String confirmationText = forgotPasswordPage.getSuccessMessageText();
        Assert.assertFalse(
                confirmationText.isBlank(),
                "TC-05 FAIL: Password reset confirmation message text was empty."
        );
    }
}
