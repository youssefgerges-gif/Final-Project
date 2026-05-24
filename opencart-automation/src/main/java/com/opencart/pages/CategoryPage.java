package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryPage extends BasePage {

    private final By sortByDropdown      = By.id("input-sort");
    private final By productPrices       = By.cssSelector(".price");
    private final By productItems        = By.cssSelector(".product-thumb");
    private final By productNames        = By.cssSelector(".product-thumb h4 a");
    private final By paginationLinks     = By.cssSelector(".pagination li a");
    private final By addToCompareButtons = By.cssSelector("button[onclick*='compare.add']");
    private final By successMessage      = By.cssSelector(".alert-success");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void sortBy(String optionText) {
        selectByVisibleText(sortByDropdown, optionText);
        waitForVisible(productItems);
    }

    public List<Double> getProductPricesAsDoubles() {
        List<WebElement> priceEls = driver.findElements(productPrices);
        return priceEls.stream()
                       .map(el -> el.getText().split("\n")[0].replaceAll("[^0-9.]", ""))
                       .filter(s -> !s.isEmpty())
                       .map(Double::parseDouble)
                       .collect(Collectors.toList());
    }

    public void filterByColor(String colorName) {
        By colorLink = By.xpath("//a[contains(@href,'color') and contains(text(),'" + colorName + "')]");
        if (isDisplayed(colorLink)) {
            click(colorLink);
            waitForVisible(productItems);
        }
    }

    public List<String> getVisibleProductNames() {
        List<WebElement> names = driver.findElements(productNames);
        return names.stream().map(WebElement::getText).toList();
    }

    public void goToPage(int pageNumber) {
        By pageLink = By.xpath("//ul[@class='pagination']//a[text()='" + pageNumber + "']");
        waitForClickable(pageLink).click();
        waitForVisible(productItems);
    }

    public void goToNextPage() {
        By nextBtn = By.cssSelector(".pagination li:last-child a");
        click(nextBtn);
        waitForVisible(productItems);
    }

    public void goToPreviousPage() {
        By prevBtn = By.cssSelector(".pagination li:first-child a");
        click(prevBtn);
        waitForVisible(productItems);
    }

    public boolean hasMultiplePages() {
        return driver.findElements(paginationLinks).size() > 0;
    }

    public int getProductCount() {
        return driver.findElements(productItems).size();
    }

    public void addFirstNProductsToCompare(int count) {
        List<WebElement> btns = waitForAllVisible(addToCompareButtons);
        for (int i = 0; i < Math.min(count, btns.size()); i++) {
            btns.get(i).click();
            waitForVisible(successMessage);
        }
    }

    public void clickCompareLink() {
        By compareLink = By.cssSelector("a[href*='product/compare']");
        waitForClickable(compareLink).click();
    }
}
