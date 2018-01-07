package ru.litebox.training;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class RegistrationTest extends BaseTest {

    private String registrationLink = "//a[text()='New customers click here']";
    private String firstNameInput = "//input[@name='firstname']";
    private String lastNameInput = "//input[@name='lastname']";
    private String address1Input = "//input[@name='address1']";
    private String postcodeInput = "//input[@name='postcode']";
    private String cityInput = "//input[@name='city']";
    private String countrySelect = "//select[@name='country_code']";
    private String zoneSelect = "//select[@name='zone_code']";
    private String zoneAlaska = "//select[@name='zone_code']/option[text()='Alaska']";
    private String emailInput = "//input[@name='email']";
    private String phoneInput = "//input[@name='phone']";
    private String passwordInput = "//input[@name='password']";
    private String confirmedPasswordInput = "//input[@name='confirmed_password']";
    private String createButton = "//button[@name='create_account']";
    private String logoutLink = "//a[text()='Logout']";
    private String loginButton = "//button[@name='login']";

    @Test
    public void checkRegistration() {
        driver.get("http://localhost/litecart/");
        long number = System.currentTimeMillis();
        registerUser(number);
        logoutStore();
        loginStore(number);
        logoutStore();
        System.out.println("Всё.");
    }

    private void registerUser(long number) {
        driver.findElement(By.xpath(registrationLink)).click();
        wait.until(d -> d.findElement(By.xpath(firstNameInput)).isDisplayed());
        driver.findElement(By.xpath(firstNameInput)).sendKeys("Bot");
        driver.findElement(By.xpath(lastNameInput)).sendKeys("Botov");
        driver.findElement(By.xpath(address1Input)).sendKeys("Some address");
        driver.findElement(By.xpath(postcodeInput)).sendKeys("10001");
        driver.findElement(By.xpath(cityInput)).sendKeys("New York");
        Select select = new Select(driver.findElement(By.xpath(countrySelect)));
        select.selectByVisibleText("United States");
        wait.until(d -> d.findElement(By.xpath(zoneAlaska)).isDisplayed());
        select = new Select(driver.findElement(By.xpath(zoneSelect)));
        select.selectByVisibleText("Alaska");
        driver.findElement(By.xpath(emailInput)).sendKeys("yuka+" + number + "@yandex.ru");
        driver.findElement(By.xpath(phoneInput)).sendKeys("1234567");
        driver.findElement(By.xpath(passwordInput)).sendKeys("12345");
        driver.findElement(By.xpath(confirmedPasswordInput)).sendKeys("12345");
        driver.findElement(By.xpath(createButton)).click();
        wait.until(d -> d.findElement(By.xpath(logoutLink)).isDisplayed());

    }

    private void loginStore(long number) {
        driver.findElement(By.xpath(emailInput)).sendKeys("yuka+" + number + "@yandex.ru");
        driver.findElement(By.xpath(passwordInput)).sendKeys("12345");
        driver.findElement(By.xpath(loginButton)).click();
        wait.until(d -> d.findElement(By.xpath(logoutLink)).isDisplayed());
    }

    private void logoutStore() {
        driver.findElement(By.xpath(logoutLink)).click();
        wait.until(d -> d.findElement(By.xpath(loginButton)).isDisplayed());
    }
}