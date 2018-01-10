package ru.litebox.training.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Page {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    @FindBy(xpath = "//span[@class='quantity']")
    private WebElement cartQuantitySpan;

    public Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(driver, this);
    }

    public int cartQuantity() {
        try {
            return Integer.valueOf(cartQuantitySpan.getText());
        } catch (NoSuchElementException e) {
            System.out.println("На этой странице нет виджета корзины!");
            e.printStackTrace();
            return 0;
        }
    }

    public void goBack() {
        driver.navigate().back();
    }
}