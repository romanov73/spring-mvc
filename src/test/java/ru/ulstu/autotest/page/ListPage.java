package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.ulstu.autotest.model.EmailRecord;

import java.util.ArrayList;
import java.util.List;

import static ru.ulstu.autotest.util.TestUtil.sleep;

public class ListPage extends BasePage {
    private static final String MENU_ITEM_CLASS = "nav-link";

    @FindBy(css = ".col-md-3[style*='font-weight: bold']")
    private List<WebElement> tableHeaders;

    @FindBy(css = ".row")
    private List<WebElement> emailRows;

    public ListPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        PageFactory.initElements(driver, this);
    }

    public static ListPage open(WebDriver driver, String baseUrl) {
        driver.get(baseUrl + "/list");
        sleep(2000);
        return new ListPage(driver, baseUrl);
    }

    @Override
    public boolean isLoaded() {
        waitForElementVisible(By.tagName("h2"));
        return driver.getTitle().equals("Список записей в БД");
    }

    public int getEmailCount() {
        return emailRows.size() - 1;
    }

    public List<EmailRecord> getAllEmails() {
        List<EmailRecord> emails = new ArrayList<>();
        emailRows = driver.findElements(By.className("row"));

        for (WebElement row : emailRows) {
            List<WebElement> columns = row.findElements(By.xpath("./div[contains(@class, 'col-md-3')]"));
            if (columns.size() >= 3) {
                emails.add(new EmailRecord(
                        columns.get(0).getText(), // recipient
                        columns.get(1).getText(), // subject
                        columns.get(2).getText()  // message
                ));
            }
        }
        return emails;
    }

    public EmailRecord getEmailAtIndex(int index) {
        List<EmailRecord> emails = getAllEmails();
        if (index >= 0 && index < emails.size()) {
            return emails.get(index);
        }
        return null;
    }

    public HomePage clickBackButton() {
        WebElement backButton = driver.findElement(By.linkText("Отправить другое сообщение"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", backButton);
        sleep(2000);
        backButton.click();
        return new HomePage(driver, baseUrl);
    }

    public HomePage goToHomePage() {
        driver.get(baseUrl + "/");
        return new HomePage(driver, baseUrl);
    }

    public ListPage goToListPage() {
        driver.findElement(By.linkText("Список отправленных сообщений")).click();
        return new ListPage(driver, baseUrl);
    }

    public AjaxPage goToAjaxPage() {
        driver.get(baseUrl + "/ajax");
        return new AjaxPage(driver, baseUrl);
    }

    public boolean isDisabledItemDisplayed() {
        WebElement disabledItem = driver.findElement(By.linkText("Недоступно"));
        return disabledItem.isDisplayed() && disabledItem.getAttribute("class").contains("disabled");
    }

    public int getHeaderCount() {
        return tableHeaders.size();
    }

    public boolean containsEmail(String recipient, String subject, String message) {
        List<EmailRecord> records = getAllEmails();
        System.out.println(records);
        System.out.printf("");
        return records.stream()
                .anyMatch(email -> email.getRecipient().equals(recipient) &&
                        email.getSubject().equals(subject) &&
                        email.getMessage().equals(message));
    }

    public List<String> getMenuUrls() {
        return driver
                .findElements(By.className(MENU_ITEM_CLASS))
                .stream()
                .map(item -> item.getAttribute("href"))
                .filter(href -> href != null && !href.endsWith("#"))
                .toList();
    }
}