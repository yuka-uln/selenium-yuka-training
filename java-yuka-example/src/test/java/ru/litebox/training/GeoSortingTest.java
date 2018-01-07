package ru.litebox.training;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GeoSortingTest extends BaseTest {

    private String rowLocator = "//td[@id='content']//table[@class='dataTable']//tr[contains(@class, 'row')]";  // в трёх таблицах одинаково
    private String countryCodeInCountriesRelative = ".//td[4]";
    private String countryNameInCountriesRelative = ".//td[5]";
    private String zonesQuantityRelativeLocator = ".//td[6]";
    private String zoneName = "//td[@id='content']//table[@class='dataTable']/tbody/tr/td[3][input[@type='hidden']]";
    private String countryLinkByCode = "//td[@id='content']//table[@class='dataTable']//tr[contains(@class, 'row')]/td[4][text()='%s']/../td[5]/a";
    private String geoZoneLinkByNumber = "//td[@id='content']//table[@class='dataTable']//tr[contains(@class, 'row')][%d]/td[5]/a";
    private String countryInGeoZone = "//table[@id='table-zones']//tr[td[4]]/td[2]/select";
    private String zoneInGeoZone = "//table[@id='table-zones']//tr[td[4]]/td[3]/select";

    @Test
    public void checkCountries() {
        login();
        checkSortingCountries();
        checkSortingZonesInCountries();
    }

    @Test
    public void checkGeoZones() {
        login();
        checkSortingGeoZones();
    }

    /**
     * 1) на странице http://localhost/litecart/admin/?app=countries&doc=countries
     * а) проверить, что страны расположены в алфавитном порядке
     */
    private void checkSortingCountries() {
        System.out.println("Проверяем сортировку стран...");
        gotoMenu("Countries");
        wait.until(ExpectedConditions.titleContains("Countries"));

        Comparator<WebElement> comparator;
        comparator = Comparator.comparing(element -> element.getAttribute("class")); // сначала в списке enabled страны, потом disabled
        comparator = comparator.thenComparing(element -> getCountryName(element).toLowerCase());

        List<WebElement> countryRows = driver.findElements(By.xpath(rowLocator));
        List<WebElement> sortedCountryRows = countryRows.stream().sorted(comparator).collect(Collectors.toList());

        for (int i = 0; i < countryRows.size(); i++) {
            if (!countryRows.get(i).equals(sortedCountryRows.get(i))) {
                Assert.fail(String.format("Сортировка стран неправильная! В %d строке ждали: \"%s\", на самом деле: \"%s\"", i + 1, getCountryName(sortedCountryRows
                        .get(i)), getCountryName(countryRows.get(i))));
            }
        }
        System.out.println("Сортировка стран проверена.");
    }

    /**
     * Выделяет название страны из строки. Используется ненормализованное значение.
     * @param element строка с названием страны
     * @return название страны
     */
    private String getCountryName(WebElement element) {
        return element.findElement(By.xpath(countryNameInCountriesRelative)).getAttribute("textContent");
    }

    /**
     * б) для тех стран, у которых количество зон отлично от нуля --
     * открыть страницу этой страны и там проверить, что зоны расположены в алфавитном порядке
     */
    private void checkSortingZonesInCountries() {
        System.out.println("Проверяем сортировку зон в странах, где они есть...");
        List<WebElement> countries = driver.findElements(By.xpath(rowLocator));

        List<String> countriesWithZones = countries
                .stream()
                .filter(element -> !"0".equals(element.findElement(By.xpath(zonesQuantityRelativeLocator)).getText()))
                .map(element -> element.findElement(By.xpath(countryCodeInCountriesRelative)).getText())
                .collect(Collectors.toList());

        for (String country : countriesWithZones) {
            checkZones(country);
        }
    }

    /**
     * Проверяет сортировку зон одной страны.
     * @param country код страны, которую нужно проверить
     */
    private void checkZones(String country) {
        System.out.println("Проверяем сортировку зон в стране с кодом " + country + "...");
        gotoMenu("Countries");
        wait.until(ExpectedConditions.titleContains("Countries"));
        driver.findElement(By.xpath(String.format(countryLinkByCode, country))).click();
        wait.until(ExpectedConditions.titleContains("Edit Country"));

        List<WebElement> zoneElements = driver.findElements(By.xpath(zoneName));
        List<String> zoneNames = zoneElements
                .stream()
                .map(element -> element.getAttribute("textContent").toLowerCase())
                .collect(Collectors.toList());
        List<String> sortedZoneNames = zoneNames.stream().sorted().collect(Collectors.toList());

        for (int i = 0; i < zoneNames.size(); i++) {
            Assert.assertEquals(String.format("Сортировка зон в стране с кодом %s неправильная!", country), sortedZoneNames
                    .get(i), zoneNames.get(i));
        }
        System.out.println("Сортировка зон в стране с кодом " + country + " проверена.");
    }

    /**
     * 2) на странице http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones
     * зайти в каждую из стран и проверить, что зоны расположены в алфавитном порядке
     */
    private void checkSortingGeoZones() {
        System.out.println("Проверяем сортировку геозон...");
        gotoMenu("Geo Zones");
        wait.until(ExpectedConditions.titleContains("Geo Zones"));

        List<WebElement> zoneRows = driver.findElements(By.xpath(rowLocator));
        for (int i = 0; i < zoneRows.size(); i++) {
            checkGeoZone(i + 1);
        }
        System.out.println("Сортировка геозон проверена.");
    }

    /**
     * Проверяет сортировку зон в геозоне.
     * @param zoneNumber номер геозоны
     */
    private void checkGeoZone(int zoneNumber) {
        System.out.println(String.format("Проверяем сортировку зон в геозоне № %d...", zoneNumber));
        gotoMenu("Geo Zones");
        wait.until(ExpectedConditions.titleContains("Geo Zones"));
        driver.findElement(By.xpath(String.format(geoZoneLinkByNumber, zoneNumber))).click();
        wait.until(ExpectedConditions.titleContains("Edit Geo Zone"));

        List<String> countryNames = driver
                .findElements(By.xpath(countryInGeoZone))
                .stream()
                .map(element -> (new Select(element)).getFirstSelectedOption().getAttribute("textContent"))
                .collect(Collectors.toList());

        List<String> zoneNames = driver
                .findElements(By.xpath(zoneInGeoZone))
                .stream()
                .map(element -> (new Select(element)).getFirstSelectedOption().getAttribute("textContent"))
                .collect(Collectors.toList());

        for (int i = 0; i < countryNames.size() - 1; i++) {
            int compare = countryNames.get(i).compareToIgnoreCase(countryNames.get(i + 1));
            if ((compare > 0) || ((compare == 0) && (zoneNames.get(i).compareToIgnoreCase(zoneNames.get(i + 1)) > 0))) {
                Assert.fail(String.format("Сортировка зон в геозоне № %d неправильная! " + "Строка № %d: \"%s\" / \"%s\" идёт раньше, чем: \"%s\" / \"%s\"", zoneNumber, i + 1, countryNames
                        .get(i), zoneNames.get(i), countryNames.get(i + 1), zoneNames.get(i + 1)));
            }
        }
    }
}
