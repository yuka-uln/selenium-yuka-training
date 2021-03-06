package ru.litebox.training;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
    protected static final String USERNAME = "admin";
    protected static final String PASSWORD = "admin";
    protected WebDriver driver;
    protected WebDriverWait wait;

    private String menuItemByName = "//ul[@id='box-apps-menu']/li/a[span[@class='name' and text()='%s']]";

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            checkBrowserLogs();
            driver.quit();
        }));
    }

    public void gotoMenu(String itemName) {
        driver.findElement(By.xpath(String.format(menuItemByName, itemName))).click();
    }

    protected void checkBrowserLogs() {
        LogEntries browserLogs;
        browserLogs = driver.manage().logs().get("browser");
        if (browserLogs.getAll().isEmpty()) {
            System.out.println("Логи браузера пусты.");
        } else {
            System.out.println("Внимание! Логи браузера:");
            browserLogs.forEach(System.out::println);
        }
    }
}
