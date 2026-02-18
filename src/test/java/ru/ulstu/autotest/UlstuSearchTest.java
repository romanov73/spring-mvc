package ru.ulstu.autotest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.ulstu.autotest.page.SearchPage;

import java.time.Duration;

public class UlstuSearchTest {
    private final static String APP_URL = "https://ulstu.ru/";
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        // WebDriverManager.firefoxdriver().setup();
        // WebDriverManager.edgedriver().setup();
        // WebDriverManager.chromedriver().driverVersion("114.0.5735.90").setup();
    }

    @BeforeEach
    public void setUp() {
        // Настройка ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Запуск в headless режиме (без UI)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Для отладки (с UI)
        // options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);

        // Альтернатива с Firefox
        // FirefoxOptions options = new FirefoxOptions();
        // options.addArguments("--headless");
        // driver = new FirefoxDriver(options);

        // Настройка неявных ожиданий
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        // Явные ожидания
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //@Test
    public void testResultPageHeader() {
        driver.get(APP_URL);
        String searchString = "Факультет информационных систем и технологий";

        SearchPage page = PageFactory.initElements(driver, SearchPage.class);
        page.sendSearchString(searchString);
        page.clickSearch();
        Assertions.assertTrue(page.isResultPresent(searchString));
    }
}
