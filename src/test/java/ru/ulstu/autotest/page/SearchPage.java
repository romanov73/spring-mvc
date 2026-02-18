package ru.ulstu.autotest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchPage {
    private WebDriver driver;

    @FindBy(className = "search-input")
    private WebElement searchInput;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public SearchPage sendSearchString(String text) {
        searchInput.sendKeys(text);
        return this;
    }

    public void clickSearch() {
        searchInput.sendKeys(Keys.RETURN);
    }

    public boolean isResultPresent(String searchText) {
        String resultItemClass = "search-results__item";
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(By.className(resultItemClass)));
        return driver.findElements(By.className(resultItemClass))
                .stream()
                .anyMatch(e -> e.getText().contains(searchText));
    }
}
