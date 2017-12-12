package ru.litebox.training;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StickersTest extends BaseTest {

    private String productBox = "//li[contains(@class, 'product')]"; // либо cssSelector: "li.product"
    private String stickerRelativeLocator = ".//div[contains(@class, 'sticker')]"; // либо cssSelector: "div.sticker"
    private String nameRelativeLocator = ".//div[contains(@class, 'name')]"; // либо cssSelector: "div.name"

    @Test
    public void checkStickers() {
        driver.get("http://localhost/litecart/");
        List<WebElement> products = driver.findElements(By.xpath(productBox));
        for (WebElement product : products) {
            checkProduct(product);
        }
        System.out.println("Ну вот, у всех товаров по одному стикеру.");
    }

    private void checkProduct(WebElement product) {
        List<WebElement> stickers = product.findElements(By.xpath(stickerRelativeLocator));
        String name = product.findElement(By.xpath(nameRelativeLocator)).getText();
        System.out.println("Проверяем товар " + name);
        if (stickers.size() != 1) {
            System.out.println(String.format("  Эгей, у товара %s количество стикеров - %d!", name, stickers.size()));
            Assert.fail();
        }
    }
}
