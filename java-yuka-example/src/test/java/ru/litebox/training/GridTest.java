package ru.litebox.training;

import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class GridTest {
    private final static String GRID_URL = "http://localhost:4444/wd/hub"; // для локального грида
//  private final static String GRID_URL = "http://USER:PASS@URL.gridlastic.com:80/wd/hub"; // облако (актуализировать)
    private RemoteWebDriver driverFirefox;
    private RemoteWebDriver driverChrome;
    private RemoteWebDriver driverIE;

    @Before
    public void start() throws MalformedURLException {
        driverFirefox = new RemoteWebDriver(new URL(GRID_URL), DesiredCapabilities.firefox());
        driverChrome = new RemoteWebDriver(new URL(GRID_URL), DesiredCapabilities.chrome());
        driverIE = new RemoteWebDriver(new URL(GRID_URL), DesiredCapabilities.internetExplorer());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            checkBrowserLogs(driverFirefox);
            checkBrowserLogs(driverChrome);
            checkBrowserLogs(driverIE);
            driverFirefox.quit();
            driverChrome.quit();
            driverIE.quit();
        }));
    }

    @Test
    public void gridTest() {
        driverFirefox.get("http://litebox.ru/");
        takeScreenshot(driverFirefox);
        driverChrome.get("http://eo.wikipedia.org/wiki/Speciala%C4%B5o:Hazarda_pa%C4%9Do");
        takeScreenshot(driverChrome);
        driverIE.get("http://selenium2.ru");
        takeScreenshot(driverIE);

        System.out.println("Тест грида закончен.");
    }

    private void takeScreenshot(RemoteWebDriver driver) {
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        String shotName = "/tmp/shot" + new Random().nextInt(1000000) + ".png";
        try {
            Files.copy(scrFile, new File(shotName));
            System.out.println("Снят скриншот " + shotName);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить скриншот " + shotName);
            e.printStackTrace();
        }
    }

    private void checkBrowserLogs(RemoteWebDriver driver) {
        LogEntries browserLogs;
        try {
            browserLogs = driver.manage().logs().get("browser");
        } catch (Exception e) {
            System.out.println(String.format("Достать логи браузера %s не удалось.", getBrowserName(driver)));
            return;
        }
        if (browserLogs.getAll().isEmpty()) {
            System.out.println(String.format("Логи браузера %s пусты.", getBrowserName(driver)));
        } else {
            System.out.println(String.format("Внимание! Логи браузера %s:", getBrowserName(driver)));
            browserLogs.forEach(System.out::println);
        }
    }

    private String getBrowserName(RemoteWebDriver driver) {
        Capabilities caps = driver.getCapabilities();
        return caps != null ? caps.getBrowserName() : driver.toString();
    }
}