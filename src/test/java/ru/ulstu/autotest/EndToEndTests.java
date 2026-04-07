package ru.ulstu.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.ulstu.autotest.page.AjaxPage;
import ru.ulstu.autotest.page.HomePage;
import ru.ulstu.autotest.page.ListPage;
import ru.ulstu.autotest.page.ResultPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.ulstu.autotest.util.TestUtil.sleep;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndTests extends BaseSeleniumTest {

    @Test
    @Order(1)
    @DisplayName("Полный пользовательский сценарий")
    public void testCompleteUserJourney() {
        String testSubject = "E2E Test " + System.currentTimeMillis();
        String testRecipient = "e2e@test.com";
        String testMessage = "End-to-end testing message";

        HomePage homePage = HomePage.open(driver, baseUrl);
        assertTrue(homePage.isLoaded());

        ResultPage resultPage = homePage
                .fillForm(testSubject, testRecipient, testMessage)
                .submitForm();

        assertTrue(resultPage.isLoaded());
        assertTrue(resultPage.isSubjectPresent(testSubject));
        assertTrue(resultPage.isRecipientPresent(testRecipient));
        assertTrue(resultPage.isMessagePresent((testMessage)));

        homePage = resultPage.clickBackLink();
        assertTrue(homePage.isLoaded());

        ListPage listPage = homePage.getNavigationBar().goToListPage();
        assertTrue(listPage.isLoaded());

        boolean found = false;
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts && !found; i++) {
            if (i > 0) {
                sleep(2000);
                listPage = ListPage.open(driver, baseUrl);
            }
            found = listPage.containsEmail(testRecipient, testSubject, testMessage);
        }
        assertTrue(found, "Запись должна быть найдена в списке");

        AjaxPage ajaxPage = listPage.getNavigationBar().goToAjaxPage();
        assertTrue(ajaxPage.isLoaded());

        sleep(2000);
        var ajaxRecords = ajaxPage.getDisplayedRecords();
        System.out.printf(">>> %s %s %s", testRecipient, testSubject, testMessage);

        boolean ajaxFound = ajaxRecords.stream()
                .anyMatch(r -> r.getRecipient().equals(testRecipient) &&
                        r.getSubject().equals(testSubject) &&
                        r.getMessage().equals(testMessage));

        assertTrue(ajaxFound, "Запись должна быть найдена через AJAX");

        homePage = ajaxPage.getNavigationBar().goToHomePage();
        assertTrue(homePage.isLoaded());
    }

    @Test
    @Order(2)
    @DisplayName("Тест навигации между всеми страницами")
    public void testNavigationBetweenAllPages() {
        HomePage homePage = HomePage.open(driver, baseUrl);

        ListPage listPage = homePage.getNavigationBar().goToListPage();
        assertTrue(listPage.isLoaded());

        AjaxPage ajaxPage = listPage.getNavigationBar().goToAjaxPage();
        assertTrue(ajaxPage.isLoaded());

        homePage = ajaxPage.getNavigationBar().goToHomePage();
        assertTrue(homePage.isLoaded());

        ajaxPage = homePage.getNavigationBar().goToAjaxPage();
        assertTrue(ajaxPage.isLoaded());

        listPage = ajaxPage.getNavigationBar().goToListPage();
        assertTrue(listPage.isLoaded());

        homePage = listPage.clickBackButton();
        assertTrue(homePage.isLoaded());
    }
}