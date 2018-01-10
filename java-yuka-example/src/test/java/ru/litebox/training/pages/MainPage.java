package ru.litebox.training.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MainPage extends Page {

    @FindBy(xpath = "//div[@id='cart']/a[@class='link']")
    private WebElement checkoutLink;

    @FindBy(xpath = "//li[contains(@class, 'product')]//a[@class='link']")
    private List<WebElement> productLinks;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public CartPage gotoCheckout() {
        checkoutLink.click();
        return new CartPage(driver);
    }

    public ProductPage openProduct(int number) {
        productLinks.get(number - 1).click();
        return new ProductPage(driver);
    }

    public MainPage addFirstProductToCart() {
        ProductPage productPage = openProduct(1);
        productPage.addToCart();
        goBack();
        return new MainPage(driver);
    }
}
