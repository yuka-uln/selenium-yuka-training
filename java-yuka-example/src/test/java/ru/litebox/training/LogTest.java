package ru.litebox.training;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class LogTest extends BaseTest {

    private String editProductLink = "//tr[contains(@class, 'row')][td[3][img]]//a[@title='Edit']";
    private String productNameHeader = "//td[@id='content']//h1";

    @Test
    public void checkLog() {
        login();
        driver.get("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
        wait.until(ExpectedConditions.titleContains("Catalog"));
        checkBrowserLogs();

        List<WebElement> productLinks = driver.findElements(By.xpath(editProductLink));
        System.out.println("Товаров на странице категории: " + productLinks.size());
        int i = 0;

        while (i < productLinks.size()) {
            productLinks.get(i).click();
            wait.until(ExpectedConditions.titleContains("Edit Product: "));
            String header = driver.findElement(By.xpath(productNameHeader)).getText();
            System.out.println("Зашли на страницу " + header);
            checkBrowserLogs();

            driver.navigate().back();
            System.out.println("Вернулись обратно.");
            checkBrowserLogs();

            productLinks = driver.findElements(By.xpath(editProductLink));
            i++;
        }
        System.out.println("Все товары на странице категории пройдены.");
    }
}