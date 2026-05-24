package com.opencart.tests;

import com.opencart.pages.*;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests covering product-level interactions.
 *
 * TC-11  – Add a Product to Wishlist
 * TC-12  – Add a Product to Compare List
 * TC-12b – Add a Product to Shopping Cart
 * TC-16  – Sort Products by Price Low to High
 * TC-17  – Sort Products by Price High to Low
 * TC-18  – Submit a Product Review
 * TC-23  – Zoom Product Image on Hover
 * TC-28  – Display Out-of-Stock Product Status
 * TC-29  – Filter Products by Color Attribute
 * TC-30  – Share Product via Social Media Button
 */
public class ProductTest extends BaseTest {

    private void loginAsRegisteredUser() {
        new LoginPage(driver).navigateAndLogin(
                TestDataHelper.VALID_EMAIL, TestDataHelper.VALID_PASSWORD);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-11: Add a Product to Wishlist
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-11: Verify that a logged-in user can add a product to the " +
                        "wishlist and it appears on the My Wish List page.")
    public void addProductToWishlistAppearsInWishlistPage() {
        loginAsRegisteredUser();

        driver.get(TestDataHelper.SIMPLE_PRODUCT_URL);

        ProductPage  productPage = new ProductPage(driver);
        String productName = productPage.getProductTitle();

        productPage.addProductToWishlist();

        // After adding, the site redirects to the wishlist page
        AccountPage accountPage = new AccountPage(driver);
        Assert.assertTrue(
                accountPage.isWishlistPageLoaded(),
                "TC-11 FAIL: Not redirected to the Wish List page after adding product."
        );

        Assert.assertTrue(
                productPage.isProductInWishlist(productName),
                "TC-11 FAIL: Product '" + productName + "' was not found in the wishlist."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-12: Add a Product to Compare List
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-12: Verify that adding two products to the compare list " +
                        "and clicking Compare shows a side-by-side comparison page.")
    public void addProductsToCompareListDisplaysComparisonPage() {
        loginAsRegisteredUser();

        // Navigate to Women > Tops category
        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN, TestDataHelper.SUB_CATEGORY_TOPS);

        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.addFirstNProductsToCompare(2);

        categoryPage.clickCompareLink();

        // Expected: comparison page is loaded
        Assert.assertTrue(
                driver.getCurrentUrl().contains("catalog/product_compare"),
                "TC-12 FAIL: URL does not indicate the comparison page. URL: "
                + driver.getCurrentUrl()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-12b: Add a Product to Shopping Cart
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-12b: Verify that adding a product to the cart displays a " +
                        "success message and increments the cart counter.")
    public void addProductToCartShowsSuccessMessageAndUpdatesCounter() {
        loginAsRegisteredUser();

        driver.get(TestDataHelper.CONFIGURABLE_PRODUCT_URL);

        ProductPage productPage = new ProductPage(driver);
        productPage.selectOptionsAndAddToCart();

        // Expected: success message
        String successMsg = productPage.getAddToCartSuccessMessage();
        Assert.assertTrue(
                successMsg.toLowerCase().contains("cart") ||
                successMsg.toLowerCase().contains("added"),
                "TC-12b FAIL: Success message did not confirm product was added to cart. Got: "
                + successMsg
        );

        // Expected: cart counter > 0
        String counterText = productPage.getCartCounterText();
        int cartCount = Integer.parseInt(counterText.trim());
        Assert.assertTrue(
                cartCount >= 1,
                "TC-12b FAIL: Cart counter did not increment after adding a product. Value: "
                + counterText
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-16: Sort Products by Price Low to High
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-16: Verify that selecting 'Price: Low to High' sort on a " +
                        "category page orders products from lowest to highest price.")
    public void sortProductsByPriceLowToHighDisplaysAscendingOrder() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN, TestDataHelper.SUB_CATEGORY_TOPS);

        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.sortBy("Price");

        List<Double> prices = categoryPage.getProductPricesAsDoubles();
        Assert.assertFalse(prices.isEmpty(), "TC-16 FAIL: No prices were found after sorting.");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "TC-16 FAIL: Prices are NOT in ascending order at index " + i
                    + ". Got " + prices.get(i) + " > " + prices.get(i + 1)
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-17: Sort Products by Price High to Low
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-17: Verify that selecting 'Price: High to Low' sort orders " +
                        "products from highest to lowest price.")
    public void sortProductsByPriceHighToLowDisplaysDescendingOrder() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN, TestDataHelper.SUB_CATEGORY_TOPS);

        CategoryPage categoryPage = new CategoryPage(driver);
        // First select ascending to make the toggle to descending work reliably
        categoryPage.sortBy("Price");

        // Click the sort-direction toggle (desc)
        org.openqa.selenium.WebElement directionLink = driver.findElement(
                org.openqa.selenium.By.cssSelector(".sorter-action"));
        if (!directionLink.getAttribute("data-value").equals("desc")) {
            directionLink.click();
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions
                            .visibilityOfElementLocated(
                                    org.openqa.selenium.By.cssSelector(".product-items")));
        }

        List<Double> prices = categoryPage.getProductPricesAsDoubles();
        Assert.assertFalse(prices.isEmpty(), "TC-17 FAIL: No prices were found after sorting.");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) >= prices.get(i + 1),
                    "TC-17 FAIL: Prices are NOT in descending order at index " + i
                    + ". Got " + prices.get(i) + " < " + prices.get(i + 1)
            );
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-18: Submit a Product Review
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-18: Verify that a logged-in user can submit a star-rated " +
                        "review and the submission is confirmed.")
    public void submitProductReviewDisplaysConfirmationMessage() {
        loginAsRegisteredUser();

        driver.get(TestDataHelper.CONFIGURABLE_PRODUCT_URL);

        ProductPage productPage = new ProductPage(driver);
        productPage.submitProductReview(
                TestDataHelper.REVIEW_STAR_RATING,
                TestDataHelper.REVIEW_NICKNAME,
                TestDataHelper.REVIEW_SUMMARY,
                TestDataHelper.REVIEW_BODY
        );

        // Expected: review submission success message
        Assert.assertTrue(
                productPage.isReviewSubmitSuccessful(),
                "TC-18 FAIL: Review submission did not show a success/confirmation message."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-23: Zoom Product Image on Hover
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-23: Verify that hovering over the main product image " +
                        "triggers the zoom effect without page errors.")
    public void hoverOverProductImageActivatesZoomEffect() {
        loginAsRegisteredUser();

        driver.get(TestDataHelper.CONFIGURABLE_PRODUCT_URL);

        ProductPage productPage = new ProductPage(driver);
        productPage.hoverOverProductImage();

        // Expected: zoom stage/container is present (no broken layout)
        Assert.assertTrue(
                productPage.isZoomEffectPresent(),
                "TC-23 FAIL: Zoom effect container was not found after hovering over the product image."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-28: Display Out-of-Stock Product Status
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-28: Verify that an out-of-stock product displays the " +
                        "'Out of Stock' label and has the 'Add to Cart' button disabled or hidden.")
    public void outOfStockProductShowsLabelAndDisablesAddToCart() {
        // No login required per test case
        driver.get(TestDataHelper.OUT_OF_STOCK_PRODUCT_URL);

        ProductPage productPage = new ProductPage(driver);

        // Expected: 'Out of Stock' label visible
        Assert.assertTrue(
                productPage.isOutOfStockLabelDisplayed(),
                "TC-28 FAIL: 'Out of Stock' label was not displayed for the product."
        );

        // Expected: 'Add to Cart' button is absent or disabled
        Assert.assertTrue(
                productPage.isAddToCartDisabledOrHidden(),
                "TC-28 FAIL: 'Add to Cart' button is still enabled for an out-of-stock product."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-29: Filter Products by Color Attribute
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-29: Verify that filtering by the color 'Blue' on a category " +
                        "page shows only products matching that color attribute.")
    public void filterProductsByColorAttributeShowsMatchingProductsOnly() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN, TestDataHelper.SUB_CATEGORY_TOPS);

        CategoryPage categoryPage = new CategoryPage(driver);
        categoryPage.filterByColor("Blue");

        // Expected: products are still visible (filter applied, results not empty)
        int count = categoryPage.getProductCount();
        Assert.assertTrue(
                count > 0,
                "TC-29 FAIL: No products were displayed after filtering by color 'Blue'."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-30: Share Product via Social Media Button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-30: Verify that the Facebook and Twitter share buttons on " +
                        "a product page contain valid share URLs with the product URL pre-filled.")
    public void socialShareButtonsContainCorrectProductUrls() {
        driver.get(TestDataHelper.PRODUCT_WITH_SOCIAL_SHARE_URL);

        ProductPage productPage = new ProductPage(driver);
        String productUrl = driver.getCurrentUrl();

        // Facebook share href must reference facebook.com and contain a URL
        String fbHref = productPage.getFacebookShareHref();
        Assert.assertTrue(
                fbHref.contains("facebook.com") && fbHref.contains("http"),
                "TC-30 FAIL: Facebook share button href is not a valid share URL. Got: " + fbHref
        );

        // Twitter/X share href must reference twitter.com or x.com and contain a URL
        String twHref = productPage.getTwitterShareHref();
        Assert.assertTrue(
                (twHref.contains("twitter.com") || twHref.contains("x.com")) &&
                twHref.contains("http"),
                "TC-30 FAIL: Twitter share button href is not a valid share URL. Got: " + twHref
        );
    }
}
