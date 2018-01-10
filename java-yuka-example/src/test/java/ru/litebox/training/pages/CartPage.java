package ru.litebox.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends Page {

    @FindBy(xpath = "//li[@class='shortcut']/a")
    private List<WebElement> productShortcuts;

    @FindBy(xpath = "//button[@name='remove_cart_item']")
    private WebElement removeButton;

    @FindBy(id = "checkout-cart-wrapper")
    private WebElement cartWrapper;

    private String cartSummaryDiv = "//div[@id='box-checkout-summary']";

    public CartPage(WebDriver driver) {
        super(driver);
        wait.until(d -> cartWrapper.isDisplayed());
        System.out.println("Перешли в корзину. Позиций в корзине: " + cartLinesQuantity());
    }

    public CartPage stopCarousel() {
        if (cartLinesQuantity() > 1) {
            productShortcuts.get(0).click();
        }
        return this;
    }

    public CartPage deleteFirstProduct() {
        System.out.println("Удаляем один товар.");
        if (cartIsEmpty()) {
            System.out.println("Но корзина уже пуста.");
        } else {
            WebElement summary = driver.findElement(By.xpath(cartSummaryDiv));
            removeButton.click();
            wait.until(ExpectedConditions.stalenessOf(summary));
            System.out.println("Позиций в корзине осталось: " + cartLinesQuantity());
        }
        return this;
    }

    public boolean cartIsEmpty() {
        return driver.findElements(By.xpath(cartSummaryDiv)).size() == 0;
    }

    public int cartLinesQuantity() {
        int shortcuts = productShortcuts.size();
        if ((0 == shortcuts) && (!cartIsEmpty())) {
            return 1;
        }
        return shortcuts;
    }
}