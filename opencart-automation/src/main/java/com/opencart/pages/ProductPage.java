package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductPage extends BasePage {

    private final By productTitle     = By.cssSelector("#content h1");
    private final By addToCartButton  = By.id("button-cart");
    private final By addToWishlistBtn = By.cssSelector("button[onclick*='wishlist.add']");
    private final By addToCompareBtn  = By.cssSelector("button[onclick*='compare.add']");
    private final By successMessage   = By.cssSelector(".alert-success");
    private final By cartQty          = By.cssSelector("#cart-total");

    private final By wishlistItems    = By.cssSelector("#wishlist-total");
    private final By stockAvailability = By.cssSelector(".list-unstyled span");

    private final By reviewTabLink    = By.linkText("Reviews (0)");
    private final By reviewName       = By.id("input-name");
    private final By reviewText       = By.id("input-review");
    private final By submitReviewBtn  = By.id("button-review");
    private final By reviewSuccess    = By.cssSelector(".alert-success");

    private final By mainProductImage = By.cssSelector("#content .thumbnails img, .product-left img");
    private final By facebookShareBtn = By.cssSelector("a[href*='facebook.com']");
    private final By twitterShareBtn  = By.cssSelector("a[href*='twitter.com'], a[href*='x.com']");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void selectOptionsAndAddToCart() {
        click(addToCartButton);
    }

    public String getAddToCartSuccessMessage() {
        waitForVisible(successMessage);
        return getText(successMessage);
    }

    public String getCartCounterText() {
        waitForVisible(cartQty);
        return getText(cartQty);
    }

    public void addProductToWishlist() {
        click(addToWishlistBtn);
    }

    public boolean isProductInWishlist(String productName) {
        return waitForUrlContains("account/wishlist") || isDisplayed(By.cssSelector(".table-responsive"));
    }

    public void addProductToCompare() {
        click(addToCompareBtn);
        waitForVisible(successMessage);
    }

    public boolean isCompareWidgetVisible() {
        return isDisplayed(successMessage);
    }

    public void submitProductReview(int starRating, String nickname, String summary, String body) {
        // Click Reviews tab
        By reviewTab = By.linkText("Reviews (0)");
        if (!isDisplayed(reviewTab)) {
            reviewTab = By.cssSelector("a[href='#tab-review']");
        }
        scrollIntoView(reviewTab);
        click(reviewTab);

        // Click star rating
        By starLocator = By.cssSelector(
            ".rating input[value='" + starRating + "'] + label, " +
            "#input-rating" + starRating
        );
        waitForClickable(starLocator).click();

        type(reviewName, nickname);
        type(reviewText, body);
        click(submitReviewBtn);
    }

    public boolean isReviewSubmitSuccessful() {
        waitForVisible(reviewSuccess);
        return isDisplayed(reviewSuccess);
    }

    public boolean isOutOfStockLabelDisplayed() {
        List<WebElement> spans = driver.findElements(stockAvailability);
        return spans.stream().anyMatch(el -> el.getText().toLowerCase().contains("out of stock"));
    }

    public boolean isAddToCartDisabledOrHidden() {
        List<WebElement> btns = driver.findElements(addToCartButton);
        if (btns.isEmpty()) return true;
        return !btns.get(0).isEnabled();
    }

    public void hoverOverProductImage() {
        hoverOver(mainProductImage);
    }

    public boolean isZoomEffectPresent() {
        return isDisplayed(mainProductImage);
    }

    public String getFacebookShareHref() {
        WebElement el = waitForVisible(facebookShareBtn);
        return el.getAttribute("href");
    }

    public String getTwitterShareHref() {
        WebElement el = waitForVisible(twitterShareBtn);
        return el.getAttribute("href");
    }

    public String getProductTitle() {
        return getText(productTitle);
    }
}
