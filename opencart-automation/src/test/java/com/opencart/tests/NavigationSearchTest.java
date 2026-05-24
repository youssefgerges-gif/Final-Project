package com.opencart.tests;

import com.opencart.pages.LoginPage;
import com.opencart.pages.NavigationPage;
import com.opencart.pages.SearchPage;
import com.opencart.utils.TestDataHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests covering search and navigation scenarios.
 *
 * TC-06 – Search for a Product
 * TC-07 – Switch Currency from USD to Euro
 * TC-08 – Select Main and Sub Categories Randomly
 * TC-09 – Hover Over Main and Sub Categories
 * TC-10 – Select Different Categories While Logged In
 */
public class NavigationSearchTest extends BaseTest {

    // Convenience login helper – shared across multiple tests in this class
    private void loginAsRegisteredUser() {
        new LoginPage(driver).navigateAndLogin(
                TestDataHelper.VALID_EMAIL, TestDataHelper.VALID_PASSWORD);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-06: Search for a Product – via Search Bar – when user is logged in
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-06: Verify that searching for a keyword returns relevant " +
                        "product results on the search results page.")
    public void searchForProductDisplaysRelevantResults() {
        loginAsRegisteredUser();

        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchForProduct(TestDataHelper.SEARCH_KEYWORD);

        // Expected: search results page shows matching products
        Assert.assertTrue(
                searchPage.hasSearchResults(),
                "TC-06 FAIL: No search results were displayed for keyword '"
                + TestDataHelper.SEARCH_KEYWORD + "'."
        );

        // Verify at least one product name contains the keyword (case-insensitive)
        List<String> productNames = searchPage.getProductNamesInResults();
        boolean anyMatch = productNames.stream()
                .anyMatch(name -> name.toLowerCase()
                                      .contains(TestDataHelper.SEARCH_KEYWORD.toLowerCase()));
        Assert.assertTrue(
                anyMatch,
                "TC-06 FAIL: No product name contained the keyword '"
                + TestDataHelper.SEARCH_KEYWORD + "'. Results: " + productNames
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-07: Switch Currency from USD to Euro – via Currency Selector
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-07: Verify that switching the currency to Euro updates " +
                        "all product prices to display the € symbol.")
    public void switchCurrencyToEuroUpdatesAllPrices() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.switchCurrencyToEuro();

        // Expected: all prices are shown in Euro (€)
        Assert.assertTrue(
                navPage.arePricesDisplayedInEuro(),
                "TC-07 FAIL: After switching to Euro, prices did not display the € symbol."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-08: Select Main and Sub Categories Randomly – on Navigation Menu
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-08: Verify that hovering over 'Women' and clicking 'Tops' " +
                        "loads the correct sub-category page with the right breadcrumb.")
    public void selectMainAndSubCategoryLoadsCorrectPage() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverMainAndClickSubCategory(
                TestDataHelper.MAIN_CATEGORY_WOMEN,
                TestDataHelper.SUB_CATEGORY_TOPS
        );

        // Expected: sub-category page loads with correct title/breadcrumb
        String pageTitle = navPage.getCategoryPageTitle();
        Assert.assertTrue(
                pageTitle.toLowerCase().contains("tops") ||
                pageTitle.toLowerCase().contains("women"),
                "TC-08 FAIL: Category page title did not match expected sub-category. Got: " + pageTitle
        );

        String breadcrumb = navPage.getBreadcrumbText();
        Assert.assertFalse(
                breadcrumb.isBlank(),
                "TC-08 FAIL: Breadcrumb trail was empty after navigating to sub-category."
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-09: Hover Over Main and Sub Categories – on Navigation Menu
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-09: Verify that hovering over a main category reveals " +
                        "the sub-category dropdown without causing navigation errors.")
    public void hoverOverMainCategoryRevealsSubCategoryDropdown() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);
        navPage.hoverOverMainCategory(TestDataHelper.MAIN_CATEGORY_WOMEN);

        // Expected: sub-category items become visible on hover
        Assert.assertTrue(
                navPage.isSubCategoryDropdownVisible(TestDataHelper.SUB_CATEGORY_TOPS),
                "TC-09 FAIL: Sub-category dropdown did not appear when hovering over '"
                + TestDataHelper.MAIN_CATEGORY_WOMEN + "'."
        );

        // Page URL must not change (no unintended navigation)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(
                currentUrl.contains("catalogsearch") || currentUrl.contains("category"),
                "TC-09 FAIL: Hovering caused unexpected navigation. URL is: " + currentUrl
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC-10: Select Different Categories While Logged In – via Navigation Menu
    // ─────────────────────────────────────────────────────────────────────────
    @Test(description = "TC-10: Verify that navigating between multiple categories " +
                        "loads each page correctly without session issues.")
    public void navigatingMultipleCategoriesLoadsEachPageCorrectly() {
        loginAsRegisteredUser();

        NavigationPage navPage = new NavigationPage(driver);

        // Navigate to 'Men'
        navPage.clickMainCategory(TestDataHelper.MAIN_CATEGORY_MEN);
        String menTitle = navPage.getCategoryPageTitle();
        Assert.assertTrue(
                menTitle.toLowerCase().contains("men"),
                "TC-10 FAIL: 'Men' category page title did not match. Got: " + menTitle
        );

        // Then navigate to 'Gear'
        navPage.clickMainCategory(TestDataHelper.MAIN_CATEGORY_GEAR);
        String gearTitle = navPage.getCategoryPageTitle();
        Assert.assertTrue(
                gearTitle.toLowerCase().contains("gear"),
                "TC-10 FAIL: 'Gear' category page title did not match. Got: " + gearTitle
        );

        // Navigate into a sub-category under Gear
        navPage.hoverMainAndClickSubCategory(TestDataHelper.MAIN_CATEGORY_GEAR, "Bags");
        String bagsTitle = navPage.getCategoryPageTitle();
        Assert.assertFalse(
                bagsTitle.isBlank(),
                "TC-10 FAIL: Sub-category 'Bags' page title was empty – page may not have loaded."
        );
    }
}
