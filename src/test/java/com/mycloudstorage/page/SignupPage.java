package com.mycloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ResourceBundle;

public class SignupPage extends Page {

    private String SIGNUP_URL;

    @FindBy(id = "inputFirstName")
    private WebElement firstName;

    @FindBy(id = "inputLastName")
    private WebElement lastName;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "buttonSignUp")
    private WebElement buttonSignUp;

    public SignupPage(WebDriver driver) {
      super();
      PageFactory.initElements(driver, this);
      SIGNUP_URL = this.testProperties.getString("SIGNUP_URL");
    }

    public void signUp(String firstName, String lastName, String username, String password) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.buttonSignUp.click();
    }

    public void navigateAndPrepareToSignup(int port, WebDriver driver, WebDriverWait wait)
    {
      goToSignup(port, driver);
      wait.until(ExpectedConditions.elementToBeClickable(username));
    }

    private void goToSignup(int port, WebDriver driver) {
      driver.get(HTTP_LOCALHOST + port + SIGNUP_URL);
    }

}
