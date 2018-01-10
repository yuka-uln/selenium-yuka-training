package ru.litebox.training.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.litebox.training.app.Application;
import ru.litebox.training.pages.CartPage;
import ru.litebox.training.pages.MainPage;

public class PageObjectsCartTest extends PageObjectsBaseTest {

    @Test
    public void checkCart() {
        Application app = new Application(driver);
        MainPage mainPage = app.gotoShop();

        for (int i = 0; i < 3; i++) {
            mainPage.addFirstProductToCart();
        }

        CartPage cartPage = mainPage.gotoCheckout().stopCarousel();

        while (!cartPage.cartIsEmpty()) {
            cartPage.deleteFirstProduct();
        }
        System.out.println("Корзина пуста.");
    }

    @Test
    public void checkCartFluently() {

        // То же самое, но на любителя Fluent
        Assert.assertTrue("Корзина не пуста!", new Application(driver)
                .gotoShop()
                .addFirstProductToCart()
                .addFirstProductToCart()
                .addFirstProductToCart()
                .gotoCheckout()
                .stopCarousel()
                .deleteFirstProduct()
                .deleteFirstProduct()
                .deleteFirstProduct()
                .cartIsEmpty());

        System.out.println("Корзина пуста, да.");
    }
}