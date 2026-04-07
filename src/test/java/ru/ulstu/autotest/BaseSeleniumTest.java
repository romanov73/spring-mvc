package ru.ulstu.autotest;

import email.Application;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static ru.ulstu.autotest.util.TestUtil.sleep;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class BaseSeleniumTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        // WebDriverManager.firefoxdriver().setup();
        // WebDriverManager.edgedriver().setup();
        // WebDriverManager.chromedriver().driverVersion("114.0.5735.90").setup();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
        waitForApplicationToStart();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Запуск в headless режиме (без UI)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        //options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);

        // Альтернатива с Firefox
        // FirefoxOptions options = new FirefoxOptions();
        // options.addArguments("--headless");
        // driver = new FirefoxDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void waitForApplicationToStart() {
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                URL url = new URI(baseUrl).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Application is ready on " + baseUrl);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Waiting for application to start... Attempt " + (attempt + 1));
            }

            sleep(1000);
            attempt++;
        }
        throw new RuntimeException("Application failed to start on " + baseUrl);
    }
}