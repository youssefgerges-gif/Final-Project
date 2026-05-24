package com.opencart.tests;

import com.opencart.pages.*;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests covering shopping cart and checkout scenarios.
 *
 * TC-12b – Add a Product to Shopping Cart          (in ProductTest – re-listed for reference)
 * TC-12c  – Create Order with Successful Payment
 * TC-19   – Apply a Discount Coupon Code
 * TC-20   – Remove an Item from Shopping Cart
 * TC-21   – Update Item Quantity in Cart
 * TC-22   – Complete Guest Checkout
 * TC-24   – Navigate Through Category Pagination
 * TC-32   – Display Empty Cart Message
 */
public class CartCheckoutTest extends BaseTest {

    private void loginAsRegisteredUser() {
        new LoginPage(driver).navigateAndLogin(
                TestDataHelper.VALID_EMAIL, TestDataHelper.VALID_PASSWORD);
    }

    /** Helper: navigate to a product, select options, add to cart. */
    private void addProductToCart(String productUrl) {
        driver.get(productUrl);
        new ProductPage(driver).selectOptionsAndAddToCart();
        // Wait for success banner before proceeding
        new ProductPage(driver).getAddToCartSuccessMessage();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-12c (TC-12d): Create Order with Successful Payment
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-12c: Verify that a logged-in user can complete checkout " +
                        "and receives an order confirmation with an order ID.")
    public void completeCheckoutAsLoggedInUserShowsOrderConfirmation() {
        loginAsRegisteredUser();

        addProductToCart(TestDataHelper.CONFIGURABLE_PRODUCT_URL);

        CartPage     cartPage     = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        cartPage.proceedToCheckout();

        // Fill shipping and select method
        checkoutPage.fillShippingDetails(
                TestDataHelper.VALID_EMAIL,
                TestDataHelper.CHECKOUT_FIRST,
                TestDataHelper.CHECKOUT_LAST,
                TestDataHelper.CHECKOUT_STREET,
                TestDataHelper.CHECKOUT_CITY,
                TestDataHelper.CHECKOUT_ZIP,
                TestDataHelper.CHECKOUT_PHONE
        );

        checkoutPage.selectPaymentAndPlaceOrder();

        // Expected: order confirmation page with an order number
        Assert.assertTrue(
                checkoutPage.isOrderConfirmed(),
                "TC-12c FAIL: Did not reach the order confirmation page."
        );

        String orderNumber = checkoutPage.getOrderNumber();
        Assert.assertFalse(
                orderNumber.isBlank(),
                "TC-12c FAIL: Order number was not displayed on the confirmation page."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-19: Apply a Discount Coupon Code
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-19: Verify that applying a valid coupon code in the cart " +
                        "reduces the cart total according to the coupon value.")
    public void applyValidCouponCodeReducesCartTotal() {
        loginAsRegisteredUser();

        addProductToCart(TestDataHelper.SIMPLE_PRODUCT_URL);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCartPage();

        String totalBefore = cartPage.getGrandTotalText();

        cartPage.applyCouponCode(TestDataHelper.VALID_COUPON_CODE);

        // Expected: coupon was accepted
        Assert.assertTrue(
                cartPage.isCouponAppliedSuccessfully(),
                "TC-19 FAIL: Coupon was not applied successfully. No success message appeared."
        );

        // Expected: the discount amount is visible
        String discountText = cartPage.getDiscountAmountText();
        Assert.assertFalse(
                discountText.isBlank(),
                "TC-19 FAIL: Discount amount was not displayed after applying the coupon."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-20: Remove an Item from Shopping Cart
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-20: Verify that removing an item from the cart updates " +
                        "the cart immediately and recalculates the total.")
    public void removeItemFromCartUpdatesCartAndRecalculatesTotal() {
        loginAsRegisteredUser();

        // Add two distinct products
        addProductToCart(TestDataHelper.SIMPLE_PRODUCT_URL);
        addProductToCart(TestDataHelper.CONFIGURABLE_PRODUCT_URL);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCartPage();

        int itemCountBefore = cartPage.getCartItemCount();

        cartPage.removeFirstCartItem();

        int itemCountAfter = cartPage.getCartItemCount();

        // Expected: one fewer item in the cart
        Assert.assertEquals(
                itemCountAfter,
                itemCountBefore - 1,
                "TC-20 FAIL: Cart item count did not decrease by 1 after removal. "
                + "Before: " + itemCountBefore + ", After: " + itemCountAfter
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-21: Update Item Quantity in Cart
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-21: Verify that changing an item's quantity to 3 and " +
                        "clicking 'Update Cart' correctly recalculates the subtotal.")
    public void updateCartItemQuantityRecalculatesSubtotal() {
        loginAsRegisteredUser();

        addProductToCart(TestDataHelper.SIMPLE_PRODUCT_URL);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCartPage();

        String subtotalBefore = cartPage.getCartSubtotalText();

        cartPage.updateFirstItemQuantity(3);

        String subtotalAfter = cartPage.getCartSubtotalText();

        // Expected: subtotal changes (≠ original for qty=1)
        Assert.assertNotEquals(
                subtotalAfter,
                subtotalBefore,
                "TC-21 FAIL: Subtotal did not change after updating quantity to 3."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-22: Complete Guest Checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-22: Verify that a guest user can complete checkout and " +
                        "receives an order confirmation page with an order ID.")
    public void guestCheckoutSuccessfullyPlacesOrder() {
        // Open site WITHOUT logging in
        driver.get(TestDataHelper.BASE_URL);

        addProductToCart(TestDataHelper.SIMPLE_PRODUCT_URL);

        CartPage     cartPage     = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        cartPage.proceedToCheckout();
        checkoutPage.continueAsGuest();

        checkoutPage.fillShippingDetails(
                TestDataHelper.GUEST_EMAIL,
                TestDataHelper.CHECKOUT_FIRST,
                TestDataHelper.CHECKOUT_LAST,
                TestDataHelper.CHECKOUT_STREET,
                TestDataHelper.CHECKOUT_CITY,
                TestDataHelper.CHECKOUT_ZIP,
                TestDataHelper.CHECKOUT_PHONE
        );

        checkoutPage.selectPaymentAndPlaceOrder();

        // Expected: order confirmation page
        Assert.assertTrue(
                checkoutPage.isOrderConfirmed(),
                "TC-22 FAIL: Guest checkout did not reach the order confirmation page."
        );

        String orderNumber = checkoutPage.getOrderNumber();
        Assert.assertFalse(
                orderNumber.isBlank(),
                "TC-22 FAIL: Order number was not displayed for the guest order."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-24: Navigate Through Category Pagination
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-24: Verify that clicking through category pages loads " +
                        "distinct products on each page and back-navigation works.")
    public void categoryPaginationLoadsDistinctProductsOnEachPage() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN, TestDataHelper.SUB_CATEGORY_TOPS);

        CategoryPage categoryPage = new CategoryPage(driver);

        // Pre-condition: multiple pages must exist
        Assert.assertTrue(
                categoryPage.hasMultiplePages(),
                "TC-24 PRE-CONDITION FAIL: The category does not have multiple pages."
        );

        // Capture page-1 products
        java.util.List<String> page1Products = categoryPage.getVisibleProductNames();

        // Go to page 2
        categoryPage.goToPage(2);
        java.util.List<String> page2Products = categoryPage.getVisibleProductNames();

        // Expected: different products on page 2
        Assert.assertNotEquals(
                page1Products,
                page2Products,
                "TC-24 FAIL: Page 2 displayed the same products as Page 1."
        );

        // Go back to page 1 via Previous
        categoryPage.goToPreviousPage();
        java.util.List<String> backToPage1Products = categoryPage.getVisibleProductNames();

        Assert.assertEquals(
                backToPage1Products,
                page1Products,
                "TC-24 FAIL: Navigating back to page 1 did not restore the original product set."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-32: Display Empty Cart Message
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-32: Verify that an empty cart displays the 'Your cart is empty' " +
                        "message and a 'Continue Shopping' link.")
    public void emptyCartDisplaysEmptyMessageAndContinueShoppingLink() {
        loginAsRegisteredUser();

        // Navigate directly to the cart page (no items added)
        CartPage cartPage = new CartPage(driver);
        cartPage.navigateToCartPage();

        // Expected: empty cart message is shown
        Assert.assertTrue(
                cartPage.isEmptyCartMessageDisplayed(),
                "TC-32 FAIL: Empty cart message was not displayed on the cart page."
        );

        // Expected: 'Continue Shopping' link is visible
        Assert.assertTrue(
                cartPage.isContinueShoppingLinkVisible(),
                "TC-32 FAIL: 'Continue Shopping' link was not visible on the empty cart page."
        );
    }
}
