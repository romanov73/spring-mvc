package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.ulstu.autotest.model.EmailRecord;

import java.util.ArrayList;
import java.util.List;

import static ru.ulstu.autotest.util.TestUtil.sleep;

public class AjaxPage extends BasePage {
    private static final String MENU_ITEM_CLASS = "nav-link";
    public static final String AJAX_PAGE_TITLE = "Динамический список записей в БД";

    @FindBy(id = "records")
    private WebElement recordsContainer;

    @FindBy(className = "btn-primary")
    private WebElement backButton;

    @FindBy(id = "editModal")
    private WebElement modal;

    @FindBy(id = "editId")
    private WebElement editId;

    @FindBy(id = "editSubject")
    private WebElement editSubject;

    @FindBy(id = "editRecipient")
    private WebElement editRecipient;

    @FindBy(id = "editMessage")
    private WebElement editMessage;

    @FindBy(css = ".modal-footer .btn-secondary")
    private WebElement modalCloseButton;

    @FindBy(css = ".modal-footer .btn-primary")
    private WebElement modalSaveButton;

    @FindBy(css = "#records .row")
    private List<WebElement> recordRows;

    public AjaxPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        PageFactory.initElements(driver, this);
    }

    public static AjaxPage open(WebDriver driver, String baseUrl) {
        driver.get(baseUrl + "/ajax");
        sleep(1000);
        return new AjaxPage(driver, baseUrl);
    }

    @Override
    public boolean isLoaded() {
        waitForElementVisible(By.id("records"));
        return driver.getTitle().equals(AJAX_PAGE_TITLE);
    }

    public List<EmailRecord> getDisplayedRecords() {
        sleep(2000);
        List<EmailRecord> emails = new ArrayList<>();
        List<WebElement> emailRows = driver.findElements(By.id("records"));

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
        emails.forEach(System.out::println);
        return emails;
    }

    public void openEditModal(int recordIndex) {
        if (recordIndex < recordRows.size()) {
            WebElement editButton = recordRows.get(recordIndex).findElement(By.xpath("./div[contains(@class, 'col-md-2')]"));
            editButton.click();
            waitForModalToOpen();
        }
    }

    private void waitForModalToOpen() {
        wait.until(ExpectedConditions.attributeContains(modal, "class", "show"));
    }

    public boolean isModalDisplayed() {
        return modal.isDisplayed() && modal.getAttribute("class").contains("show");
    }

    public void closeModal() {
        modalCloseButton.click();
        wait.until(ExpectedConditions.attributeContains(modal, "class", "fade"));
    }

    public void fillModalForm(String subject, String recipient, String message) {
        editSubject.clear();
        editSubject.sendKeys(subject);
        editRecipient.clear();
        editRecipient.sendKeys(recipient);
        editMessage.clear();
        editMessage.sendKeys(message);
    }

    public void saveModalForm() {
        modalSaveButton.click();
        sleep(1000);
        wait.until(ExpectedConditions.attributeContains(modal, "class", "fade"));
    }

    public String getModalId() {
        return editId.getText();
    }

    public HomePage clickBackButton() {
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
        driver.get(baseUrl + "/list");
        return new ListPage(driver, baseUrl);
    }

    public int getRecordCount() {
        return recordRows.size();
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