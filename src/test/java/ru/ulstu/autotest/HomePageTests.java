package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.page.HomePage;
import ru.ulstu.autotest.page.ResultPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomePageTests extends BaseSeleniumTest {
    public static final String HOME_PAGE_TITLE = "Простая обработка формы на Spring MVC";


    @Test
    @Order(1)
    @DisplayName("Тест загрузки главной страницы")
    public void testHomePageLoad() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        assertTrue(homePage.isLoaded());
        assertTrue(homePage.isFormDisplayed());
    }

    @Test
    @Order(2)
    @DisplayName("Тест отправки формы")
    public void testFormSubmission() {
        HomePage homePage = HomePage.open(driver, baseUrl);

        ResultPage resultPage = homePage
                .fillForm("Тестовая тема", "test@example.com", "Тестовое сообщение")
                .submitForm();

        assertTrue(resultPage.isLoaded());
        assertTrue(resultPage.isSubjectPresent("Тестовая тема"));
        assertTrue(resultPage.isRecipientPresent("test@example.com"));
        assertTrue(resultPage.isMessagePresent("Тестовое сообщение"));
        assertTrue(resultPage.allResultsDisplayed());
    }

    @Test
    @Order(3)
    @DisplayName("Тест возврата на главную страницу")
    public void testBackToHome() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        ResultPage resultPage = homePage
                .fillForm("Тест", "back@test.com", "Back test")
                .submitForm();

        HomePage returnedHomePage = resultPage.clickBackLink();
        assertTrue(returnedHomePage.isLoaded());
    }

    @Test
    @Order(4)
    @DisplayName("Тест валидации формы")
    public void testFormValidation() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.submitFormExpectingError();
        assertTrue(homePage.isFormDisplayed());
        assertTrue(homePage.isErrorDisplayed());
    }

    @Test
    @Order(5)
    @DisplayName("Тест очистки формы")
    public void testFormClear() {
        HomePage homePage = HomePage.open(driver, baseUrl);
        homePage.fillForm("Тест", "test@test.com", "Message");
        assertEquals("Тест", homePage.getSubjectValue());
        assertEquals("test@test.com", homePage.getRecipientValue());
        assertEquals("Message", homePage.getMessageValue());
        homePage.clearForm();
        assertEquals("", homePage.getSubjectValue());
        assertEquals("", homePage.getRecipientValue());
        assertEquals("", homePage.getMessageValue());
    }
}