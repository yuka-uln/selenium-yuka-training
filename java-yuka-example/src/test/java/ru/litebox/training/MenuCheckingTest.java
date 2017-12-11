package ru.litebox.training;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class MenuCheckingTest extends BaseTest {

    private String menuItems = "//ul[@id='box-apps-menu']/li/a";
    private String innerMenuItems = "//ul[@class='docs']/li/a";
    private String menuItemByName = "//ul[@id='box-apps-menu']/li/a[span[@class='name' and text()='%s']]";
    private String innerMenuItemByName = "//ul[@class='docs']/li/a[span[@class='name' and text()='%s']]";
    private String header = "//h1";

    @Test
    public void checkMenu() {
        login();
        List<WebElement> menu = driver.findElements(By.xpath(menuItems));
        List<String> itemNames = menu.stream().map(WebElement::getText).collect(Collectors.toList());
        for (String s : itemNames) {
            checkMenuItem(s, menu.size());
        }
    }

    private void checkMenuItem(String itemName, int menuSize) {
        System.out.println("Жмём пункт меню: " + itemName);
        driver.findElement(By.xpath(String.format(menuItemByName, itemName))).click();
        checkHeader();
        List<WebElement> innerMenu = driver.findElements(By.xpath(innerMenuItems));
        List<String> innerItemNames = innerMenu.stream().map(WebElement::getText).collect(Collectors.toList());
        for (String s : innerItemNames) {
            checkInnerMenuItem(s, innerMenu.size());
        }

        // Иногда в зависимости от выбранного пункта меню меняется вся конфигурация меню.
        // Проверим, нет ли сюрпризов здесь - хотя бы количество пунктов в меню должно совпадать с изначальным.
        Assert.assertEquals(menuSize, driver.findElements(By.xpath(menuItems)).size());
    }

    private void checkInnerMenuItem(String innerItemName, int innerMenuSize) {
        System.out.println("    Жмём вложенный пункт меню: " + innerItemName);
        driver.findElement(By.xpath(String.format(innerMenuItemByName, innerItemName))).click();
        checkHeader();
        Assert.assertEquals(innerMenuSize, driver.findElements(By.xpath(innerMenuItems)).size());
    }

    private void checkHeader() {
        List<WebElement> headers = driver.findElements(By.xpath(header));
        Assert.assertEquals(1, headers.size());
        System.out.println("                Заголовок есть: " + headers.get(0).getText());
    }
}
