package ru.litebox.training;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LitecartTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
//        driver = new ChromeDriver();
//        driver = new FirefoxDriver();
//        driver = new InternetExplorerDriver();
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    private boolean loggedIn() {
        return !driver.findElements(By.id("sidebar")).isEmpty();
    }

    private void loginTest() {
        driver.get("http://localhost/litecart/admin/");
        if (loggedIn()) {
            System.out.println("Ой, кажется, мы уже залогинены... Разлогинимся, чтобы проверить процедуру логина.");
            driver.findElement(By.xpath("//a[@title='Logout']")).click();
            wait.until(d -> !d.findElements(By.id("box-login")).isEmpty());
        }

        System.out.println("Логинимся...");
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(USERNAME);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@name='login']")).click();
        wait.until(d -> loggedIn());
        System.out.println("Успешно.");
    }

    @Test
    public void loginTestOnce() {
        System.out.println("Попробуем залогиниться.");
        loginTest();
    }

    @Test
    public void loginTestTwice() {
        System.out.println("Попробуем два раза.");
        loginTest();
        loginTest();
    }

    @After
    public void stop() {
        driver.quit();
    }

}
