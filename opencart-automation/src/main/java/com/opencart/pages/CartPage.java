package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartButton        = By.id("cart");
    private final By viewCartLink      = By.cssSelector("a[href*='checkout/cart']");
    private final By checkoutButton    = By.cssSelector("a[href*='checkout/checkout']");
    private final By cartItems         = By.cssSelector("#content .table-responsive tbody tr");
    private final By removeItemBtn     = By.cssSelector("button[data-original-title='Remove']");
    private final By quantityInput     = By.cssSelector("input.form-control[name='quantity']");
    private final By updateCartBtn     = By.cssSelector("button[data-original-title='Update']");
    private final By cartTotal         = By.cssSelector("#cart-total");
    private final By subTotal          = By.cssSelector("#content .table tfoot tr:first-child td:last-child");
    private final By couponInput       = By.id("input-coupon");
    private final By applyCouponBtn    = By.id("button-coupon");
    private final By successMessage    = By.cssSelector(".alert-success");
    private final By emptyCartMessage  = By.cssSelector("#content p");
    private final By continueShopBtn   = By.cssSelector("#content .btn");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCartPage() {
        driver.get("https://awesomeqa.com/ui/index.php?route=checkout/cart");
        waitForVisible(By.cssSelector("#content"));
    }

    public void proceedToCheckout() {
        navigateToCartPage();
        waitForClickable(checkoutButton).click();
    }

    public void applyCouponCode(String couponCode) {
        waitForVisible(couponInput);
        type(couponInput, couponCode);
        click(applyCouponBtn);
        waitForVisible(successMessage);
    }

    public boolean isCouponAppliedSuccessfully() {
        return isDisplayed(successMessage);
    }

    public String getDiscountAmountText() {
        return isDisplayed(successMessage) ? getText(successMessage) : "";
    }

    public void removeFirstCartItem() {
        List<WebElement> removeBtns = waitForAllVisible(removeItemBtn);
        removeBtns.get(0).click();
        waitForVisible(By.cssSelector("#content"));
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void updateFirstItemQuantity(int newQuantity) {
        List<WebElement> qtyFields = waitForAllVisible(quantityInput);
        qtyFields.get(0).clear();
        qtyFields.get(0).sendKeys(String.valueOf(newQuantity));
        List<WebElement> updateBtns = waitForAllVisible(updateCartBtn);
        updateBtns.get(0).click();
        waitForVisible(subTotal);
    }

    public String getCartSubtotalText() {
        return isDisplayed(subTotal) ? getText(subTotal) : "";
    }

    public String getGrandTotalText() {
        return isDisplayed(cartTotal) ? getText(cartTotal) : "";
    }

    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(emptyCartMessage) &&
               getText(emptyCartMessage).toLowerCase().contains("empty");
    }

    public boolean isContinueShoppingLinkVisible() {
        return isDisplayed(continueShopBtn);
    }
}
