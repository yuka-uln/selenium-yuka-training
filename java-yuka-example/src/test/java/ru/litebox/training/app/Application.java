package ru.litebox.training.app;

import org.openqa.selenium.WebDriver;
import ru.litebox.training.pages.MainPage;

public class Application {

    private final WebDriver driver;

    public Application(WebDriver driver) {
        this.driver = driver;
    }

    public MainPage gotoShop() {
        driver.get("http://localhost/litecart/");
        return new MainPage(driver);
    }
}