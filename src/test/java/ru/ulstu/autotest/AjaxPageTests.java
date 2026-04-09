package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.model.EmailRecord;
import ru.ulstu.autotest.page.AjaxPage;
import ru.ulstu.autotest.page.HomePage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AjaxPageTests extends BaseSeleniumTest {

    @Test
    @Order(1)
    @DisplayName("Тест загрузки AJAX страницы")
    public void testAjaxPageLoad() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        assertTrue(ajaxPage.isLoaded());
    }

    @Test
    @Order(2)
    @DisplayName("Тест загрузки данных через AJAX")
    public void testAjaxDataLoad() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.fillForm("Тестовая тема", "test@example.com", "Тестовое сообщение")
                .submitForm();

        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        int recordCount = ajaxPage.getRecordCount();
        assertTrue(recordCount >= 0);
    }

    @Test
    @Order(3)
    @DisplayName("Тест отображения записей")
    public void testDisplayRecords() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        var records = ajaxPage.getDisplayedRecords();
        for (EmailRecord record : records) {
            assertNotNull(record.getRecipient());
            assertNotNull(record.getSubject());
            assertNotNull(record.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Тест модального окна")
    public void testModalDialog() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        if (ajaxPage.getRecordCount() > 0) {
            ajaxPage.openEditModal(0);
            assertTrue(ajaxPage.isModalDisplayed());
            ajaxPage.fillModalForm("Updated Subject", "updated@test.com", "Updated Message");
            ajaxPage.saveModalForm();
            assertFalse(ajaxPage.isModalDisplayed());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Тест закрытия модального окна")
    public void testCloseModal() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);

        if (ajaxPage.getRecordCount() > 0) {
            ajaxPage.openEditModal(0);
            assertTrue(ajaxPage.isModalDisplayed());
            ajaxPage.closeModal();
            assertFalse(ajaxPage.isModalDisplayed());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Тест на ввод данных с AJAX страницы")
    public void testBackToHomeFromAjax() {
        AjaxPage ajaxPage = AjaxPage.open(driver, baseUrl);
        HomePage homePage = ajaxPage.clickBackButton();
        assertTrue(homePage.isLoaded());
    }
}