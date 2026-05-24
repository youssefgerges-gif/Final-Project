package com.opencart.utils;

/**
 * Centralised test-data constants and generators.
 * All tests source their data from here to make maintenance easy.
 */
public final class TestDataHelper {

    private TestDataHelper() { /* utility class – no instances */ }

    // ── URLs ──────────────────────────────────────────────────────────────────
    public static final String BASE_URL = "https://awesomeqa.com/ui/index.php?route=common/home";

    // ── Registered account (must exist on the target site) ────────────────────
    public static final String VALID_EMAIL    = "youssefgergeseg@gmail.com";
    public static final String VALID_PASSWORD = "myJoe4ever";

    // ── New-account data ───────────────────────────────────────────────────────
    public static final String NEW_FIRST_NAME = "Auto";
    public static final String NEW_LAST_NAME  = "Tester";
    /** Guaranteed unique e-mail for registration tests. */
    public static String uniqueEmail() {
        return "auto_" + System.currentTimeMillis() + "@qa-test.dev";
    }
    public static final String REGISTRATION_PASSWORD = "Secure@7890";

    // ── Profile update ────────────────────────────────────────────────────────
    public static final String UPDATED_FIRST_NAME = "UpdatedFirst";
    public static final String UPDATED_LAST_NAME  = "UpdatedLast";

    // ── Password change ───────────────────────────────────────────────────────
    public static final String NEW_ACCOUNT_PASSWORD = "NewPass@2025";

    // ── Search ────────────────────────────────────────────────────────────────
    public static final String SEARCH_KEYWORD = "jacket";

    // ── Categories ────────────────────────────────────────────────────────────
    public static final String MAIN_CATEGORY_WOMEN = "Women";
    public static final String MAIN_CATEGORY_MEN   = "Men";
    public static final String MAIN_CATEGORY_GEAR  = "Gear";
    public static final String SUB_CATEGORY_TOPS   = "Tops";

    // ── Product URLs (reliable PDP links on Magento luma demo) ────────────────
    /** A simple in-stock product with no variants – safe for cart/wishlist tests. */
    public static final String SIMPLE_PRODUCT_URL =
            "https://awesomeqa.com/ui/index.php?route=product/product&product_id=40";

    /** A configurable product (size + colour) – used for add-to-cart tests. */
    public static final String CONFIGURABLE_PRODUCT_URL =
            "https://awesomeqa.com/ui/index.php?route=product/product&product_id=42";

    /** A product known to show an out-of-stock state (update if the demo resets). */
    public static final String OUT_OF_STOCK_PRODUCT_URL =
            "https://awesomeqa.com/ui/index.php?route=product/product&product_id=30";

    /** A product with social-share buttons visible. */
    public static final String PRODUCT_WITH_SOCIAL_SHARE_URL =
            "https://awesomeqa.com/ui/index.php?route=product/product&product_id=40";

    // ── Cart & coupon ─────────────────────────────────────────────────────────
    /** Coupon code that is pre-loaded on the Magento luma demo. */
    public static final String VALID_COUPON_CODE = "20poff";

    // ── Review ────────────────────────────────────────────────────────────────
    public static final int    REVIEW_STAR_RATING = 4;
    public static final String REVIEW_NICKNAME    = "AutoReviewer";
    public static final String REVIEW_SUMMARY     = "Great product!";
    public static final String REVIEW_BODY        = "This is an automated review submitted by the test suite.";

    // ── Address ───────────────────────────────────────────────────────────────
    public static final String ADDR_FIRST_NAME = "Address";
    public static final String ADDR_LAST_NAME  = "Test";
    public static final String ADDR_STREET     = "123 Main Street";
    public static final String ADDR_CITY       = "New York";
    public static final String ADDR_POSTCODE   = "10001";
    public static final String ADDR_PHONE      = "5551234567";

    // ── Checkout (guest) ──────────────────────────────────────────────────────
    public static final String GUEST_EMAIL     = "guest_" + System.currentTimeMillis() + "@qa-test.dev";
    public static final String CHECKOUT_FIRST  = "Guest";
    public static final String CHECKOUT_LAST   = "Buyer";
    public static final String CHECKOUT_STREET = "456 Oak Ave";
    public static final String CHECKOUT_CITY   = "Austin";
    public static final String CHECKOUT_ZIP    = "73301";
    public static final String CHECKOUT_PHONE  = "5559876543";
}
