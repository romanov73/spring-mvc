package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage extends BasePage {

    private static final String MENU_ITEM_CLASS = "nav-link";

    @FindBy(id = "theme")
    private WebElement subjectInput;

    @FindBy(id = "recipient")
    private WebElement recipientInput;

    @FindBy(id = "message")
    private WebElement messageInput;

    @FindBy(css = "input[type='submit']")
    private WebElement submitButton;

    @FindBy(tagName = "form")
    private WebElement form;

    public HomePage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        PageFactory.initElements(driver, this);
    }

    public static HomePage open(WebDriver driver, String baseUrl) {
        driver.get(baseUrl + "/");
        return new HomePage(driver, baseUrl);
    }

    @Override
    public boolean isLoaded() {
        waitForElementVisible(By.id("theme"));
        return driver.getTitle().equals("Простая обработка формы на Spring MVC");
    }

    public HomePage fillForm(String subject, String recipient, String message) {
        subjectInput.clear();
        subjectInput.sendKeys(subject);
        recipientInput.clear();
        recipientInput.sendKeys(recipient);
        messageInput.clear();
        messageInput.sendKeys(message);
        return this;
    }

    public ResultPage submitForm() {
        submitButton.click();
        return new ResultPage(driver, baseUrl);
    }

    public HomePage submitFormExpectingError() {
        submitButton.click();
        return this;
    }

    public boolean isFormDisplayed() {
        return form.isDisplayed() &&
                subjectInput.isDisplayed() &&
                recipientInput.isDisplayed() &&
                messageInput.isDisplayed();
    }

    public String getSubjectValue() {
        return subjectInput.getAttribute("value");
    }

    public String getRecipientValue() {
        return recipientInput.getAttribute("value");
    }

    public String getMessageValue() {
        return messageInput.getAttribute("value");
    }

    public HomePage goToHomePage() {
        driver.get(baseUrl + "/");
        return new HomePage(driver, baseUrl);
    }

    public ListPage goToListPage() {
        driver.get(baseUrl + "/list");
        return ListPage.open(driver, baseUrl);
    }

    public AjaxPage goToAjaxPage() {
        driver.get(baseUrl + "/ajax");
        return new AjaxPage(driver, baseUrl);
    }

    public boolean isDisabledItemDisplayed() {
        return driver
                .findElement(By.xpath("//*[contains(text(), 'Недоступно')]"))
                .getAttribute("class")
                .contains("disabled");
    }

    public void clickDropdown() {
        WebElement dropdown = driver.findElement(By.xpath("//a[contains(@class, 'dropdown-toggle')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
    }

    public HomePage clearForm() {
        subjectInput.clear();
        recipientInput.clear();
        messageInput.clear();
        return this;
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(By.cssSelector("p[style='color:red']")).isEmpty();
    }

    public List<String> getMenuUrls() {
        return driver
                .findElements(By.className(MENU_ITEM_CLASS))
                .stream()
                .map(item -> item.getAttribute("href"))
                .filter(href -> href != null && !href.endsWith("#"))
                .toList();
    }

    public boolean isDropdownVisible() {
        return !driver.findElement(By.className("dropdown-item")).isDisplayed();
    }
}