package ru.litebox.training;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class LitecartTest extends BaseTest {

    private enum SelectedBrowser {
        CHROME, FIREFOX, IE, EDGE, FIREFOX_LEGACY, FIREFOX_NIGHTLY
    }

    private static final SelectedBrowser SELECTED_BROWSER = SelectedBrowser.CHROME; // браузер выбирать здесь
    private static final String FIREFOX_PATH = "c:\\Program Files\\Mozilla Firefox\\firefox.exe";
    private static final String FIREFOX_LEGACY_PATH = "c:\\Program Files (x86)\\Mozilla Firefox 52 ESR EO\\firefox.exe";
    private static final String FIREFOX_NIGHTLY_PATH = "C:\\Program Files\\Nightly\\firefox.exe";

    @Before
    public void start() {

        switch (SELECTED_BROWSER) {
            case CHROME:
                driver = new ChromeDriver();
                break;
            case IE:
                driver = new InternetExplorerDriver();
                break;
            case EDGE:
                driver = new EdgeDriver();
                break;
            case FIREFOX:
                driver = new FirefoxDriver(new FirefoxOptions()
                        .setBinary(new FirefoxBinary(new File(FIREFOX_PATH))));
                break;
            case FIREFOX_LEGACY:
                driver = new FirefoxDriver(new FirefoxOptions()
                        .setLegacy(true)
                        .setBinary(new FirefoxBinary(new File(FIREFOX_LEGACY_PATH))));
                break;
            case FIREFOX_NIGHTLY:
                driver = new FirefoxDriver(new FirefoxOptions()
                        .setBinary(new FirefoxBinary(new File(FIREFOX_NIGHTLY_PATH))));
                break;
        }

        wait = new WebDriverWait(driver, 10);
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
