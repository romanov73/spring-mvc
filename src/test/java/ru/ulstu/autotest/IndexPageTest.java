package ru.ulstu.autotest;

import email.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ulstu.autotest.page.IndexPage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
class IndexPageTest extends BaseSeleniumTest {

    private IndexPage indexPage;

    @BeforeEach
    void setUpLoginPage() {
        driver.get(baseUrl);
        indexPage = new IndexPage(driver);
    }

    @Test
    @DisplayName("Переход на страницу со списком")
    void shouldNavigate() {
        indexPage.clickToList();
        assertThat(driver.getCurrentUrl()).contains("/list");
    }
}