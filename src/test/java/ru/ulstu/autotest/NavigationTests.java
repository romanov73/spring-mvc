package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.page.AjaxPage;
import ru.ulstu.autotest.page.HomePage;
import ru.ulstu.autotest.page.ListPage;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.ulstu.autotest.HomePageTests.HOME_PAGE_TITLE;
import static ru.ulstu.autotest.ListPageTests.LIST_PAGE_TITLE;
import static ru.ulstu.autotest.page.AjaxPage.AJAX_PAGE_TITLE;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NavigationTests extends BaseSeleniumTest {

    public static final Map<String, String> PAGES_TITLES = Map.of(
            "/", HOME_PAGE_TITLE,
            "/list", LIST_PAGE_TITLE,
            "/ajax", AJAX_PAGE_TITLE);

    @Test
    @DisplayName("Тест навигационного меню на главной странице")
    public void testNavigationFromHomePage() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.getMenuUrls().forEach((url) -> {
            driver.get(url);
            assertEquals(driver.getCurrentUrl(), url);
            assertEquals(driver.getTitle(), PAGES_TITLES.get(url.substring(url.lastIndexOf("/"))));
        });
    }

    @Test
    @DisplayName("Тест навигационного меню на странице списка")
    public void testNavigationFromListPage() {
        ListPage listPage = ListPage.open(driver, baseUrl);
        listPage.getMenuUrls().forEach((url) -> {
            driver.get(url);
            assertEquals(driver.getCurrentUrl(), url);
            assertEquals(driver.getTitle(), PAGES_TITLES.get(url.substring(url.lastIndexOf("/"))));
        });
    }

    @Test
    @DisplayName("Тест навигационного меню на AJAX странице")
    public void testNavigationFromAjaxPage() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        ajaxPage.getMenuUrls().forEach((url) -> {
            driver.get(url);
            assertEquals(driver.getCurrentUrl(), url);
            assertEquals(driver.getTitle(), PAGES_TITLES.get(url.substring(url.lastIndexOf("/"))));
        });
    }

    @Test
    @DisplayName("Тест недоступного пункта меню")
    public void testDisabledMenuItem() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        assertTrue(homePage.isDisabledItemDisplayed());
    }

    @Test
    @DisplayName("Тест выпадающего меню")
    public void testDropdownMenu() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.clickDropdown();
        assertTrue(homePage.isDropdownVisible());
    }
}