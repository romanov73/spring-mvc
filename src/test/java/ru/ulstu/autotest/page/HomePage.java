package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

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

    private final NavigationBar navigationBar;

    public HomePage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        PageFactory.initElements(driver, this);
        this.navigationBar = new NavigationBar(driver);
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

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public HomePage clearForm() {
        subjectInput.clear();
        recipientInput.clear();
        messageInput.clear();
        return this;
    }
}