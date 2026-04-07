package ru.ulstu.autotest.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavigationBar {
    private WebDriver driver;

    @FindBy(linkText = "Главная страница")
    private WebElement homeLink;

    @FindBy(linkText = "Список отправленных сообщений")
    private WebElement listLink;

    @FindBy(linkText = "Динамическая страница")
    private WebElement ajaxLink;

    @FindBy(linkText = "Выпадающее меню")
    private WebElement dropdownMenu;

    @FindBy(linkText = "Недоступно")
    private WebElement disabledItem;

    @FindBy(css = ".navbar-brand")
    private WebElement brandName;

    public NavigationBar(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public HomePage goToHomePage() {
        homeLink.click();
        return new HomePage(driver, getBaseUrl());
    }

    public ListPage goToListPage() {
        listLink.click();
        return new ListPage(driver, getBaseUrl());
    }

    public AjaxPage goToAjaxPage() {
        ajaxLink.click();
        return new AjaxPage(driver, getBaseUrl());
    }

    public boolean isDisabledItemDisplayed() {
        return disabledItem.isDisplayed() && disabledItem.getAttribute("class").contains("disabled");
    }

    public void clickDropdown() {
        dropdownMenu.click();
    }

    private String getBaseUrl() {
        return driver.getCurrentUrl().substring(0,
                driver.getCurrentUrl().indexOf("/", 8));
    }
}