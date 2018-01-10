package ru.litebox.training.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;

public class ProductPage extends Page {

    @FindBy(xpath = "//div[@id='box-product']//h1")
    private WebElement productNameHeader;

    @FindBy(xpath = "//div[@id='box-product']//td[span[@class='required']]/select")
    private List<WebElement> requiredSelects;

    @FindBy(xpath = "//button[@name='add_cart_product']")
    private WebElement addToCartButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage addToCart() {
        int oldQuantity = cartQuantity();
        wait.until(ExpectedConditions.visibilityOfAllElements(Arrays.asList(productNameHeader, addToCartButton)));

        String productName = productNameHeader.getText();
        System.out.println("Добавляем товар \"" + productName + "\"");

        for (WebElement element : requiredSelects) {
            wait.until(ExpectedConditions.elementToBeClickable(element)); // а то в IE не успевает
            Select select = new Select(element);
            select.selectByIndex(1);
        }

        addToCartButton.click();
        wait.until(d -> oldQuantity + 1 == cartQuantity());
        System.out.println("Товаров в корзине: " + cartQuantity());

        return this;
    }
}