package ru.litebox.training.tests;

import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;

public class PageObjectsBaseTest {
    protected WebDriver driver;

    @Before
    public void start() {
        driver = new ChromeDriver();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            checkBrowserLogs();
            driver.quit();
        }));
    }

    private void checkBrowserLogs() {
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