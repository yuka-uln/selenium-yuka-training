package ru.litebox.training;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
    protected static final String USERNAME = "admin";
    protected static final String PASSWORD = "admin";
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected boolean loggedIn() {
        return !driver.findElements(By.id("sidebar")).isEmpty();
    }

    protected void login() {
        driver.get("http://localhost/litecart/admin/");
        if (!loggedIn()) {
            System.out.println("Логинимся...");
            driver.findElement(By.xpath("//input[@name='username']")).sendKeys(USERNAME);
            driver.findElement(By.xpath("//input[@name='password']")).sendKeys(PASSWORD);
            driver.findElement(By.xpath("//button[@name='login']")).click();
            wait.until(d -> loggedIn());
            System.out.println("Успешно.");
        }
    }

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
    }
}