package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.page.AjaxPage;
import ru.ulstu.autotest.page.HomePage;
import ru.ulstu.autotest.page.ListPage;
import ru.ulstu.autotest.page.NavigationBar;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NavigationTests extends BaseSeleniumTest {

    @Test
    @DisplayName("Тест навигационного меню на главной странице")
    public void testNavigationFromHomePage() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        NavigationBar nav = homePage.getNavigationBar();
        ListPage listPage = nav.goToListPage();
        assertTrue(listPage.isLoaded());
        AjaxPage ajaxPage = nav.goToAjaxPage();
        assertTrue(ajaxPage.isLoaded());
        HomePage returnedHome = nav.goToHomePage();
        assertTrue(returnedHome.isLoaded());
    }

    @Test
    @DisplayName("Тест навигационного меню на странице списка")
    public void testNavigationFromListPage() {
        ListPage listPage = ListPage.open(driver, baseUrl);
        NavigationBar nav = listPage.getNavigationBar();
        HomePage homePage = nav.goToHomePage();
        assertTrue(homePage.isLoaded());
        AjaxPage ajaxPage = nav.goToAjaxPage();
        assertTrue(ajaxPage.isLoaded());
        ListPage returnedList = nav.goToListPage();
        assertTrue(returnedList.isLoaded());
    }

    @Test
    @DisplayName("Тест навигационного меню на AJAX странице")
    public void testNavigationFromAjaxPage() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        NavigationBar nav = ajaxPage.getNavigationBar();
        HomePage homePage = nav.goToHomePage();
        assertTrue(homePage.isLoaded());
        ListPage listPage = nav.goToListPage();
        assertTrue(listPage.isLoaded());
        AjaxPage returnedAjax = nav.goToAjaxPage();
        assertTrue(returnedAjax.isLoaded());
    }

    @Test
    @DisplayName("Тест недоступного пункта меню")
    public void testDisabledMenuItem() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        NavigationBar nav = homePage.getNavigationBar();
        assertTrue(nav.isDisabledItemDisplayed());
    }

    @Test
    @DisplayName("Тест выпадающего меню")
    public void testDropdownMenu() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        NavigationBar nav = homePage.getNavigationBar();
        nav.clickDropdown();
    }
}