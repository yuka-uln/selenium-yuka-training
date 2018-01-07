package ru.litebox.training;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;
import java.util.Random;

public class AddProductTest extends BaseTest {

    private String addNewProductLink = "//a[@class='button'][contains(@href, '&doc=edit_product')]";
    private String enabledInput = "//input[@name='status'][@value='1']";
    private String namesInput = "//input[contains(@name, 'name')]";
    private String codeInput = "//input[@name='code']";
    private String categoriesInput = "//input[contains(@name, 'categories')]";
    private String defaultCategorySelectOptions = "//select[@name='default_category_id']/option";
    private String defaultCategorySelect = "//select[@name='default_category_id']";
    private String productGroupsInput = "//input[contains(@name, 'product_groups')]";
    private String quantityInput = "//input[@name='quantity']";
    private String quantityUnitSelect = "//select[@name='quantity_unit_id']";
    private String deliveryStatusSelect = "//select[@name='delivery_status_id']";
    private String soldOutStatusSelect = "//select[@name='sold_out_status_id']";
    private String newImageInput = "//input[@name='new_images[]']";
    private String dateValidFromInput = "//input[@name='date_valid_from']";
    private String dateValidToInput = "//input[@name='date_valid_to']";

    private String informationTabLink = "//a[@href='#tab-information']";
    private String manufacturerSelectOptions = "//select[@name='manufacturer_id']/option";
    private String manufacturerSelect = "//select[@name='manufacturer_id']";
    private String supplierSelectOptions = "//select[@name='supplier_id']/option";
    private String supplierSelect = "//select[@name='supplier_id']";
    private String keywordsInput = "//input[@name='keywords']";
    private String shortDescriptionInput = "//input[contains(@name, 'short_description')]";
    private String descriptionTextarea = "//textarea[contains(@name, 'description')]";
    private String headTitleInput = "//input[contains(@name, 'head_title')]";
    private String metaDescriptionInput = "//input[contains(@name, 'meta_description')]";

    private String pricesTabLink = "//a[@href='#tab-prices']";
    private String purchasePriceInput = "//input[@name='purchase_price']";
    private String purchasePriceCurrencyCodeSelect = "//select[@name='purchase_price_currency_code']";
    private String taxClassSelectOptions = "//select[@name='tax_class_id']/option";
    private String taxClassSelect = "//select[@name='tax_class_id']";
    private String pricesInput = "//input[contains(@name, 'prices')][@data-type='currency']";
    private String saveButton = "//button[@name='save']";
    private String justAddedProductLink = "//a[text()='%s']";

    private Random random = new Random();

    @Test
    public void checkProductAdding() {
        login();
        int productNumber = random.nextInt(10000);
        String productName = "Bot Product " + productNumber;
        System.out.println("Проверяем, заведётся ли новый товар \"" + productName + "\"...");
        gotoMenu("Catalog");
        wait.until(ExpectedConditions.titleContains("Catalog"));
        driver.findElement(By.xpath(addNewProductLink)).click();
        wait.until(d -> d.findElement(By.xpath(enabledInput)).isDisplayed());

        driver.findElement(By.xpath(enabledInput)).click();

        for (WebElement element : driver.findElements(By.xpath(namesInput))) {
            element.sendKeys(productName);
        }

        driver.findElement(By.xpath(codeInput)).sendKeys("yuka" + productNumber);

        List<WebElement> categories = driver.findElements(By.xpath(categoriesInput));
        if (categories.size() > 0) {
            categories.get(random.nextInt(categories.size())).click();
        }
        if (driver.findElements(By.xpath(defaultCategorySelectOptions)).size() == 0) {
            categories.get(random.nextInt(categories.size())).click();
        }

        // если нет переводов категорий, в списке может быть 0 опций - баг Litecart
        if (driver.findElements(By.xpath(defaultCategorySelectOptions)).size() > 0) {
            Select select = new Select(driver.findElement(By.xpath(defaultCategorySelect)));
            select.selectByIndex(0);
        }

        List<WebElement> productGroups = driver.findElements(By.xpath(productGroupsInput));
        if (productGroups.size() > 0) {
            productGroups.get(random.nextInt(productGroups.size())).click();
        }

        driver.findElement(By.xpath(quantityInput)).clear();
        driver.findElement(By.xpath(quantityInput)).sendKeys("42");

        Select select = new Select(driver.findElement(By.xpath(quantityUnitSelect)));
        select.selectByIndex(1);

        select = new Select(driver.findElement(By.xpath(deliveryStatusSelect)));
        select.selectByIndex(1);

        select = new Select(driver.findElement(By.xpath(soldOutStatusSelect)));
        select.selectByIndex(1);

        String fileName = new File("src\\test\\resources\\E-marko.jpg").getAbsolutePath();
        driver.findElement(By.xpath(newImageInput)).sendKeys(fileName);

        driver.findElement(By.xpath(dateValidFromInput)).sendKeys(Keys.HOME + "03.04.2017");

        driver.findElement(By.xpath(dateValidToInput)).sendKeys(Keys.HOME + "22.11.2019");

        // вкладка Information
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(informationTabLink)));
        driver.findElement(By.xpath(informationTabLink)).click();
        wait.until(d -> d.findElement(By.xpath(manufacturerSelect)).isDisplayed());

        if (driver.findElements(By.xpath(manufacturerSelectOptions)).size() > 1) {
            select = new Select(driver.findElement(By.xpath(manufacturerSelect)));
            select.selectByIndex(1);
        }

        if (driver.findElements(By.xpath(supplierSelectOptions)).size() > 1) {
            select = new Select(driver.findElement(By.xpath(supplierSelect)));
            select.selectByIndex(1);
        }

        driver.findElement(By.xpath(keywordsInput)).sendKeys("Esperanto");

        for (WebElement element : driver.findElements(By.xpath(shortDescriptionInput))) {
            element.sendKeys("Stamp " + productNumber);
        }

        for (WebElement element : driver.findElements(By.xpath(descriptionTextarea))) {
            element.sendKeys("Stamp " + productNumber + " is here.");
        }

        for (WebElement element : driver.findElements(By.xpath(headTitleInput))) {
            element.sendKeys("Esperanto stamp " + productNumber);
        }

        for (WebElement element : driver.findElements(By.xpath(metaDescriptionInput))) {
            element.sendKeys("Stamp Esperanto " + productNumber);
        }

        // вкладка Prices
        driver.findElement(By.xpath(pricesTabLink)).click();
        wait.until(d -> d.findElement(By.xpath(purchasePriceInput)).isDisplayed());

        driver.findElement(By.xpath(purchasePriceInput)).clear();
        driver.findElement(By.xpath(purchasePriceInput)).sendKeys("123");

        select = new Select(driver.findElement(By.xpath(purchasePriceCurrencyCodeSelect)));
        select.selectByIndex(1);

        if (driver.findElements(By.xpath(taxClassSelectOptions)).size() > 1) {
            select = new Select(driver.findElement(By.xpath(taxClassSelect)));
            select.selectByIndex(1);
        }

        for (WebElement element : driver.findElements(By.xpath(pricesInput))) {
            element.clear();
            element.sendKeys("99");
        }

        driver.findElement(By.xpath(saveButton)).click();
        wait.until(d -> d.findElement(By.xpath(String.format(justAddedProductLink, productName))).isDisplayed());
        System.out.println("Да, завёлся.");
    }
}