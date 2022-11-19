package com.mycloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.ResourceBundle;

public class CredentialPage extends Page{

    @FindBy(id = "nav-credentials-tab")
    private WebElement tab;

    @FindBy(id = "buttonNewCredential")
    private WebElement createButton;

    @FindBy(id = "credential-url")
    private WebElement url;

    @FindBy(id = "credential-username")
    private WebElement username;

    @FindBy(id = "credential-password")
    private WebElement pwd;

    @FindBy(id = "buttonSaveChangesCredentials")
    private WebElement saveButton;

    @FindBy(id = "credentialTable")
    private WebElement table;

    public CredentialPage(WebDriver driver) {
      super();
      PageFactory.initElements(driver, this);
    }

    public void create(WebDriver webDriver, int port, WebDriverWait webDriverWait, String url, String username, String password) {
      goHome(webDriver, port);

      goToCredentialTab(webDriverWait);

      openCreateCredentialModal(webDriverWait);

      clickSaveButton(webDriverWait, url, username, password);
    }

  private void clickSaveButton(WebDriverWait webDriverWait, String url, String username, String password) {
    webDriverWait.until(ExpectedConditions.visibilityOf(this.url)).sendKeys(url);
    webDriverWait.until(ExpectedConditions.visibilityOf(this.username)).sendKeys(username);
    webDriverWait.until(ExpectedConditions.visibilityOf(pwd)).sendKeys(password);
    saveButton.click();
  }

  private void openCreateCredentialModal(WebDriverWait webDriverWait) {
    webDriverWait.until(ExpectedConditions.elementToBeClickable(createButton));
    createButton.click();
  }

  private void goToCredentialTab(WebDriverWait webDriverWait) {
    webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
    tab.click();
  }

  public boolean exists(WebDriver webDriver, int port, WebDriverWait webDriverWait, String url, String username, String password) {
    goHome(webDriver, port);

    goToCredentialTab(webDriverWait);

    if (findCredentialInTheCredentialsTable(webDriverWait, url, username, password)) return true;

    return false;
  }

  private boolean findCredentialInTheCredentialsTable(WebDriverWait webDriverWait, String url, String username, String pwd) {
    List<WebElement> credentialRows = table.findElements(By.className("credentialTableRow"));

    if (findCredentialInCredentialsTable(webDriverWait, url, username, pwd, credentialRows)) return true;

    return false;
  }

  private boolean findCredentialInCredentialsTable(WebDriverWait webDriverWait, String url, String username, String pwd, List<WebElement> credentialRows) {
    for (WebElement credentialRow : credentialRows) {
      WebElement urlElement = credentialRow.findElement(By.className("classCredentialUrl"));
      WebElement usernameElement = credentialRow.findElement(By.className("classCredentialUsername"));
      WebElement pwdElement = credentialRow.findElement(By.className("classCredentialPassword"));

      webDriverWait.until(ExpectedConditions.visibilityOf(urlElement));
      webDriverWait.until(ExpectedConditions.visibilityOf(usernameElement));
      webDriverWait.until(ExpectedConditions.visibilityOf(pwdElement));

      if(compareUrlAndUsernameAndPwd(url, username, pwd, urlElement, usernameElement, pwdElement)) return true;
    }
    return false;
  }

  private boolean compareUrlAndUsernameAndPwd(String url, String username, String pwd, WebElement urlElement, WebElement usernameElement, WebElement pwdElement) {
    return urlElement.getText().equals(url) && usernameElement.getText().equals(username) && !pwdElement.getText().equals(pwd);
  }

  public String getEncryptedPwd(WebDriver webDriver, int port, WebDriverWait webDriverWait, String url, String username) {
      goHome(webDriver, port);
      goToCredentialTab(webDriverWait);
      return findCredentialEncryptedPwd(webDriverWait, url, username);
  }

  private String findCredentialEncryptedPwd(WebDriverWait webDriverWait, String url, String username) {
    List<WebElement> credentialRows = table.findElements(By.className("credentialTableRow"));
    String encryptedPwd = "";
    for (WebElement credentialRow : credentialRows) {
        WebElement urlElement = credentialRow.findElement(By.className("classCredentialUrl"));
        WebElement usernameElement = credentialRow.findElement(By.className("classCredentialUsername"));
        WebElement passwordElement = credentialRow.findElement(By.className("classCredentialPassword"));

        webDriverWait.until(ExpectedConditions.visibilityOf(urlElement));
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameElement));
        webDriverWait.until(ExpectedConditions.visibilityOf(passwordElement));

        if(compareCredential(urlElement.getText(), url, usernameElement.getText(), username)) {
          encryptedPwd = passwordElement.getText();
        }
    }
    return encryptedPwd;
  }

  private static boolean compareCredential(String urlElement, String url, String usernameElement, String username) {
    return urlElement.equals(url) &&
      usernameElement.equals(username);
  }

  public void editPwd(WebDriverWait webDriverWait, String url, String username, String newPassword) {

        goToCredentialTab(webDriverWait);

        List<WebElement> credentialTableRows = table.findElements(By.className("credentialTableRow"));

        for (WebElement credentialTableRow : credentialTableRows) {

            WebElement urlElement = credentialTableRow.findElement(By.className("classCredentialUrl"));
            webDriverWait.until(ExpectedConditions.visibilityOf(urlElement));

            WebElement usernameElement = credentialTableRow.findElement(By.className("classCredentialUsername"));
            webDriverWait.until(ExpectedConditions.visibilityOf(usernameElement));

            if(compareCredential(urlElement.getText(), url, usernameElement.getText(), username)) {
                WebElement editButton = credentialTableRow.findElement(By.tagName("button"));
                webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));
                editButton.click();

                webDriverWait.until(ExpectedConditions.visibilityOf(pwd));
                pwd.clear();
                pwd.sendKeys(newPassword);
                saveButton.click();
            }
        }
  }

  public void deleteCredential(WebDriver webDriver, int port, WebDriverWait webDriverWait, String url, String username) {
      goHome(webDriver, port);
      goToCredentialTab(webDriverWait);
      findCredentialInCredentialsTableAndClickDelete(webDriverWait, url, username);
  }

  private void findCredentialInCredentialsTableAndClickDelete(WebDriverWait webDriverWait, String url, String username) {
    List<WebElement> credentialRows = table.findElements(By.className("credentialTableRow"));
    for (WebElement credentialRow : credentialRows) {
        WebElement urlElement = credentialRow.findElement(By.className("classCredentialUrl"));
        WebElement usernameElement = credentialRow.findElement(By.className("classCredentialUsername"));

        webDriverWait.until(ExpectedConditions.visibilityOf(urlElement));
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameElement));

        String urlElementText = urlElement.getText();
        String usernameElementText = usernameElement.getText();

        if(compareCredential(urlElementText, url, usernameElementText, username)) {
          clickOnDeleteButton(webDriverWait, credentialRow);
        }
    }
  }

  private static void clickOnDeleteButton(WebDriverWait webDriverWait, WebElement credentialRow) {
    WebElement deleteButton = credentialRow.findElement(By.tagName("a"));
    webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));
    deleteButton.click();
  }
}
