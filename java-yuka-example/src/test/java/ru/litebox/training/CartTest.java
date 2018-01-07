package ru.litebox.training;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CartTest extends BaseTest {

    private String checkoutLink = "//div[@id='cart']/a[@class='link']";
    private String cartSummaryDiv = "//div[@id='box-checkout-summary']";
    private String productShortcut = "//li[@class='shortcut']/a";
    private String productLink = "//li[contains(@class, 'product')]//a[@class='link']";
    private String addToCartButton = "//button[@name='add_cart_product']";
    private String productNameHeader = "//div[@id='box-product']//h1";
    private String requiredSelect = "//div[@id='box-product']//td[span[@class='required']]/select";
    private String cartQuantitySpan = "//span[@class='quantity']";
    private String removeButton = "//button[@name='remove_cart_item']";

    @Test
    public void checkCart() {
        driver.get("http://localhost/litecart/");
        for (int i = 0; i < 3; i++) {
            addFirstProductToCart();
        }

        driver.findElement(By.xpath(checkoutLink)).click();
        wait.until(d -> d.findElement(By.xpath(cartSummaryDiv)).isDisplayed());
        if (cartLinesQuantity() > 1) {
            driver.findElement(By.xpath(productShortcut)).click();  // остановить карусель
        }
        System.out.println("Перешли в корзину. Позиций в корзине: " + cartLinesQuantity());

        while (cartIsNotEmpty()) {
            deleteFirstProduct();
        }
        System.out.println("Корзина пуста.");
    }

    private void addFirstProductToCart() {
        int oldQuantity = cartQuantity();
        WebElement product = driver.findElement(By.xpath(productLink));
        product.click();
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addToCartButton)));

        String productName = driver.findElement(By.xpath(productNameHeader)).getText();
        System.out.println("Добавляем товар \"" + productName + "\"");

        for (WebElement element : driver.findElements(By.xpath(requiredSelect))) {
            wait.until(ExpectedConditions.elementToBeClickable(element)); // а то в IE не успевает
            Select select = new Select(element);
            select.selectByIndex(1);
        }

        addButton.click();
        wait.until(d -> oldQuantity + 1 == cartQuantity());
        System.out.println("Товаров в корзине: " + cartQuantity());
        driver.navigate().back();
    }

    private int cartQuantity() {
        return Integer.valueOf(driver.findElement(By.xpath(cartQuantitySpan)).getText());
    }

    private int cartLinesQuantity() {
        int shortcuts = driver.findElements(By.xpath(productShortcut)).size();
        if ((0 == shortcuts) && (cartIsNotEmpty())) {
            return 1;
        }
        return shortcuts;
    }

    private boolean cartIsNotEmpty() {
        return driver.findElements(By.xpath(cartSummaryDiv)).size() > 0;
    }

    private void deleteFirstProduct() {
        System.out.println("Удаляем один товар");
        WebElement summary = driver.findElement(By.xpath(cartSummaryDiv));
        driver.findElement(By.xpath(removeButton)).click();
        wait.until(ExpectedConditions.stalenessOf(summary));
        System.out.println("Позиций в корзине осталось: " + cartLinesQuantity());
    }
}