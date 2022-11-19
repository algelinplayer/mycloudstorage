package com.mycloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ResourceBundle;

public class LoginPage extends Page {

    private String LOGIN_URL;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
      super();
      PageFactory.initElements(driver, this);
      LOGIN_URL = testProperties.getString("LOGIN_URL");
    }

    public void login(String username, String password) {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.loginButton.click();
    }

    public WebElement getUsername() {
      return this.username;
    }

    public void navigateAndPrepareToLogin(int port, WebDriver driver, WebDriverWait wait) {
      goToLogin(port, driver);
      WebElement username = this.getUsername();
      wait.until(ExpectedConditions.elementToBeClickable(username));
    }

    private void goToLogin(int port, WebDriver driver) {
      driver.get(HTTP_LOCALHOST + port + LOGIN_URL);
    }

}
