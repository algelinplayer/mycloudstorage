package com.mycloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    public HomePage(WebDriver driver) {
      PageFactory.initElements(driver, this);
    }

    public void prepareToLogout(WebDriverWait webDriverWait)
    {
      webDriverWait.until(ExpectedConditions.elementToBeClickable(logoutButton));
    }

    public void logout() {
        this.logoutButton.click();
    }

}
