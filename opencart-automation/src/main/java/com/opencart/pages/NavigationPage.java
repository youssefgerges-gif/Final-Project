package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class NavigationPage extends BasePage {

    private final By currencyButton  = By.cssSelector(".btn-group .dropdown-toggle");
    private final By priceSymbols    = By.cssSelector(".price");
    private final By navbarCategories = By.cssSelector("#menu .nav > li > a");
    private final By breadcrumb      = By.cssSelector("#content .breadcrumb li:last-child");
    private final By categoryTitle   = By.cssSelector("#content h2, #content h1");

    public NavigationPage(WebDriver driver) {
        super(driver);
    }

    public void switchCurrencyToEuro() {
        click(currencyButton);
        By euroOption = By.cssSelector("button[name='EUR'], form[action*='EUR'] button");
        waitForVisible(euroOption);
        click(euroOption);
        waitForVisible(priceSymbols);
    }

    public boolean arePricesDisplayedInEuro() {
        List<WebElement> prices = driver.findElements(priceSymbols);
        return prices.stream().anyMatch(el -> el.getText().contains("€"));
    }

    public void hoverMainAndClickSubCategory(String mainCategory, String subCategory) {
        By mainCat = By.linkText(mainCategory);
        By subCat  = By.linkText(subCategory);
        hoverOver(mainCat);
        waitForVisible(subCat);
        click(subCat);
    }

    public void hoverOverMainCategory(String mainCategory) {
        hoverOver(By.linkText(mainCategory));
    }

    public boolean isSubCategoryDropdownVisible(String subCategory) {
        return isDisplayed(By.linkText(subCategory));
    }

    public void clickMainCategory(String mainCategory) {
        click(By.linkText(mainCategory));
    }

    public String getBreadcrumbText() {
        return isDisplayed(breadcrumb) ? getText(breadcrumb) : "";
    }

    public String getCategoryPageTitle() {
        return isDisplayed(categoryTitle) ? getText(categoryTitle) : driver.getTitle();
    }

    public List<String> getMainCategoryNames() {
        List<WebElement> links = driver.findElements(navbarCategories);
        return links.stream().map(WebElement::getText).toList();
    }
}
