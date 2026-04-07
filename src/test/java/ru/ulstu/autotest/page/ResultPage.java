package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage extends BasePage {

    @FindBy(xpath = "//div[contains(text(),'Тема')]")
    private WebElement subjectResult;

    @FindBy(xpath = "//div[contains(text(),'Кому')]")
    private WebElement recipientResult;

    @FindBy(xpath = "//div[contains(text(),'Сообщение')]")
    private WebElement messageResult;

    @FindBy(linkText = "Отправить другое сообщение")
    private WebElement backLink;

    @FindBy(tagName = "h1")
    private WebElement pageHeader;

    public ResultPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        PageFactory.initElements(driver, this);
    }

    @Override
    public boolean isLoaded() {
        waitForElementVisible(By.xpath("//h2[contains(text(),'Список записей в БД')]"));
        return driver.getTitle().equals("Список записей в БД");
    }

    public boolean isSubjectPresent(String targetSubject) {
        return !driver.findElements(By.xpath("//div[text()='" + targetSubject + "']")).isEmpty();
    }

    public boolean isRecipientPresent(String Recipient) {
        return !driver.findElements(By.xpath("//div[text()='" + Recipient + "']")).isEmpty();
    }

    public boolean isMessagePresent(String targetMessage) {
        return !driver.findElements(By.xpath("//div[text()='" + targetMessage + "']")).isEmpty();
    }

    public HomePage clickBackLink() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backLink);
        return new HomePage(driver, baseUrl);
    }

    public boolean allResultsDisplayed() {
        return subjectResult.isDisplayed() &&
                recipientResult.isDisplayed() &&
                messageResult.isDisplayed();
    }
}