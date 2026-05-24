package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage extends BasePage {

    private final By searchBar    = By.name("search");
    private final By searchButton = By.cssSelector(".btn.btn-default.btn-lg");
    private final By productItems = By.cssSelector(".product-thumb");
    private final By productNames = By.cssSelector(".product-thumb h4 a");
    private final By noResults    = By.cssSelector("#content p");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public void searchForProduct(String keyword) {
        WebElement field = waitForClickable(searchBar);
        field.clear();
        field.sendKeys(keyword);
        field.sendKeys(Keys.RETURN);
        waitForUrlContains("route=product/search");
    }

    public List<String> getProductNamesInResults() {
        List<WebElement> elements = driver.findElements(productNames);
        return elements.stream().map(WebElement::getText).toList();
    }

    public int getProductCount() {
        return driver.findElements(productItems).size();
    }

    public boolean hasSearchResults() {
        return getProductCount() > 0;
    }

    public boolean isNoResultsMessageDisplayed() {
        return isDisplayed(noResults);
    }
}
