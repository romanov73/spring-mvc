package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.model.EmailRecord;
import ru.ulstu.autotest.page.HomePage;
import ru.ulstu.autotest.page.ListPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.ulstu.autotest.util.TestUtil.sleep;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListPageTests extends BaseSeleniumTest {

    @Test
    @Order(1)
    @DisplayName("Тест загрузки страницы списка")
    public void testListPageLoad() {
        ListPage listPage = ListPage.open(driver, baseUrl);
        assertTrue(listPage.isLoaded());
        assertEquals(3, listPage.getHeaderCount());
    }

    @Test
    @Order(2)
    @DisplayName("Тест получения записей из списка")
    public void testGetEmailList() {
        ListPage listPage = ListPage.open(driver, baseUrl);
        int emailCount = listPage.getEmailCount();
        assertTrue(emailCount >= 0);
        var emails = listPage.getAllEmails();
        assertEquals(emailCount, emails.size());

        if (emailCount > 0) {
            EmailRecord firstEmail = listPage.getEmailAtIndex(0);
            assertNotNull(firstEmail);
            assertNotNull(firstEmail.getRecipient());
            assertNotNull(firstEmail.getSubject());
            assertNotNull(firstEmail.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Тест навигации со страницы списка")
    public void testNavigationFromListPage() {
        ListPage listPage = ListPage.open(driver, baseUrl);
        HomePage homePage = listPage.clickBackButton();
        assertTrue(homePage.isLoaded());
    }

    @Test
    @Order(4)
    @DisplayName("Тест создания новой записи через форму")
    public void testCreateNewEmail() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.fillForm("List Test", "list@test.com", "Testing list page")
                .submitForm();
        ListPage listPage = ListPage.open(driver, baseUrl);
        boolean found = listPage.containsEmail("list@test.com", "List Test", "Testing list page");

        if (!found) {
            sleep(2000);
            listPage = ListPage.open(driver, baseUrl);
            found = listPage.containsEmail("list@test.com", "List Test", "Testing list page");
        }
        assertTrue(found);
    }
}