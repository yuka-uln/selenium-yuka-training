package ru.litebox.training;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Set;

public class LinksTest extends BaseTest {

    private String editCountryLink = "//td[@id='content']//table[@class='dataTable']//tr[contains(@class, 'row')]/td[7]/a";
    private String externalLink = "//a[i[contains(@class,'fa-external-link')]]";

    @Test
    public void checkLinks() {
        login();
        gotoMenu("Countries");
        wait.until(ExpectedConditions.titleContains("Countries"));

        driver.findElement(By.xpath(editCountryLink)).click();
        wait.until(ExpectedConditions.titleContains("Edit Country"));

        for (WebElement link : driver.findElements(By.xpath(externalLink))) {
            checkLink(link);
        }
        System.out.println("Все ссылки проверены.");
    }

    private void checkLink(WebElement link) {
        String href = link.getAttribute("href");

        String mainWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();
        link.click();
        String newWindow = wait.until(anyWindowOtherThan(oldWindows));
        driver.switchTo().window(newWindow);
        new Actions(driver).pause(3000).perform(); // можно и почитать 3 секунды (но не в Firefox)
        driver.close();
        driver.switchTo().window(mainWindow);
        System.out.println(String.format("Ссылка \"%s\" проверена.", href));
    }

    private ExpectedCondition<String> anyWindowOtherThan(Set<String> oldWindows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver driver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
}