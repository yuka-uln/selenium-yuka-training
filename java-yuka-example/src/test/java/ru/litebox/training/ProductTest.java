package ru.litebox.training;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductTest extends BaseTest {

    private String campaignsProductBox = "//div[@id='box-campaigns']//li[contains(@class, 'product')]";
    private String detailsProductBox = "//div[@id='box-product']";

    private String nameOnMainRelative = ".//div[contains(@class, 'name')]";
    private String nameInDetailsRelative = ".//h1";
    private String regularPriceRelative = ".//s[contains(@class, 'regular-price')]";
    private String campaignPriceRelative = ".//strong[contains(@class, 'campaign-price')]";
    private String linkRelative = ".//a[@class='link']";

    private final static Pattern HEX_PATTERN = Pattern.compile("#(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})");

    private class Product {
        private String name;
        private String link;
        private String regularPrice;
        private String campaignPrice;

        private Product(WebElement box) {
            name = box.findElement(By.xpath(nameOnMainRelative)).getText();
            link = box.findElement(By.xpath(linkRelative)).getAttribute("href");
            regularPrice = box.findElement(By.xpath(regularPriceRelative)).getAttribute("textContent");
            campaignPrice = box.findElement(By.xpath(campaignPriceRelative)).getAttribute("textContent");
        }
    }

    @Test
    public void checkCampaignProducts() {
        driver.get("http://localhost/litecart/");
        List<WebElement> productBoxes = driver.findElements(By.xpath(campaignsProductBox));

        // Сначала составим список товаров (а то их порядок меняется от раза к разу) и запомним названия и цены.
        // И попутно проверим то, что можно проверить сразу.
        List<Product> products = new ArrayList<>();
        for (WebElement productBox : productBoxes) {
            Product p = new Product(productBox);
            products.add(p);
            System.out.println("Проверяем товар " + p.name);
            checkProductPrices(productBox);
        }

        for (Product product : products) {
            checkProductDetails(product);
        }
        System.out.println("Все акционные товары проверены.");
    }

    private void checkProductPrices(WebElement box) {
        try {
            String colorOfRegularPriceOnMain = box.findElement(By.xpath(regularPriceRelative)).getCssValue("color");
            String hex = Color.fromString(colorOfRegularPriceOnMain).asHex();
            System.out.println("   Цвет обычной цены = " + colorOfRegularPriceOnMain + " = " +
                    oneColor(hex, 1) + "/" + oneColor(hex, 2) + "/" + oneColor(hex, 3));
            Assert.assertTrue("Обычная цена не серая!",
                    (oneColor(hex, 1) == oneColor(hex, 2)) &&
                            (oneColor(hex, 2) == oneColor(hex, 3)));
        } catch (NoSuchElementException e) {
            Assert.fail("Не найдена зачёркнутая обычная цена!");
        }

        try {
            String colorOfCampaignPriceOnMain = box.findElement(By.xpath(campaignPriceRelative)).getCssValue("color");
            String hex = Color.fromString(colorOfCampaignPriceOnMain).asHex();
            System.out.println("   Цвет акционной цены = " + colorOfCampaignPriceOnMain + " = "
                    + oneColor(hex, 1) + "/" + oneColor(hex, 2) + "/" + oneColor(hex, 3));
            Assert.assertTrue("Акционная цена не красная!", (oneColor(hex, 2) == 0) && (oneColor(hex, 3) == 0));
        } catch (NoSuchElementException e) {
            Assert.fail("Не найдена жирная акционная цена!");
        }

        Double sizeOfRegularPriceOnMain = fontSize(box.findElement(By.xpath(regularPriceRelative)).getCssValue("font-size"));
        System.out.println("   Размер обычной цены = " + sizeOfRegularPriceOnMain);
        Double sizeOfCampaignPriceOnMain = fontSize(box.findElement(By.xpath(campaignPriceRelative)).getCssValue("font-size"));
        System.out.println("   Размер акционной цены = " + sizeOfCampaignPriceOnMain);
        Assert.assertTrue("Акционная цена не крупнее, чем обычная!", sizeOfCampaignPriceOnMain > sizeOfRegularPriceOnMain);
    }

    private void checkProductDetails(Product product) {
        System.out.println("Заходим в карточку товара " + product.name);
        driver.get(product.link);

        WebElement productDetails = driver.findElement(By.xpath(detailsProductBox));

        String nameInDetails = productDetails.findElement(By.xpath(nameInDetailsRelative)).getText();
        Assert.assertEquals("Не совпадает название!", product.name, nameInDetails);

        WebElement regularPriceInDetails = productDetails.findElement(By.xpath(regularPriceRelative));
        String valueOfRegularPriceInDetails = regularPriceInDetails.getAttribute("textContent");
        System.out.println("   Обычная цена = " + valueOfRegularPriceInDetails);
        Assert.assertEquals("Не совпадает обычная цена!", product.regularPrice, valueOfRegularPriceInDetails);

        WebElement campaignPriceInDetails = productDetails.findElement(By.xpath(campaignPriceRelative));
        String valueOfCampaignPriceInDetails = campaignPriceInDetails.getAttribute("textContent");
        System.out.println("   Акционная цена = " + valueOfCampaignPriceInDetails);
        Assert.assertEquals("Не совпадает акционная цена!", product.campaignPrice, valueOfCampaignPriceInDetails);

        checkProductPrices(productDetails);
    }

    private int oneColor(String hexColor, int number) {
        Matcher matcher = HEX_PATTERN.matcher(hexColor);
        matcher.find();
        return Integer.parseInt(matcher.group(number), 16);
    }

    private double fontSize(String font) {
        return Double.valueOf(font.substring(0, font.length()-2));
    }
}