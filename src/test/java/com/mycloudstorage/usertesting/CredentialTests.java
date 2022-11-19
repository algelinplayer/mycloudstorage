package com.mycloudstorage.usertesting;

import com.mycloudstorage.page.CredentialPage;
import com.mycloudstorage.page.HomePage;
import com.mycloudstorage.page.LoginPage;
import com.mycloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Random;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {

    private static ResourceBundle testProperties;

    private static String HTTP_LOCALHOST;
    private static String SIGNUP_URL;
    private static String LOGIN_URL;
    private static String HOME_URL;

    @LocalServerPort
    private int port;

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
      WebDriverManager.chromedriver().setup();
      testProperties = ResourceBundle.getBundle("test");
      HTTP_LOCALHOST = testProperties.getString("HTTP_LOCALHOST");
      LOGIN_URL = testProperties.getString("LOGIN_URL");
      HOME_URL = testProperties.getString("HOME_URL");
      SIGNUP_URL = testProperties.getString("SIGNUP_URL");
    }

    private void doLogin(String username, String password) {
      webDriver.get(HTTP_LOCALHOST + port + LOGIN_URL);
      LoginPage loginPage = new LoginPage(webDriver);
      loginPage.navigateAndPrepareToLogin(port, webDriver, webDriverWait);
      loginPage.login(username, password);
    }

    private void doSignup(String firstName, String lastName, String username, String password) {
      SignupPage signupPage = new SignupPage(webDriver);
      signupPage.navigateAndPrepareToSignup(port, webDriver, webDriverWait);
      signupPage.signUp(firstName, lastName, username, password);
    }

    @AfterAll
    public static void afterAll() {

    }

    private void doLogout() {
      goHome();
      HomePage homePage = new HomePage(webDriver);
      homePage.prepareToLogout(webDriverWait);
      homePage.logout();
    }

    private void goHome() {
      webDriver.get(HTTP_LOCALHOST + port + HOME_URL);
    }

    @BeforeEach
    public void beforeEach() {
      initializeDrivers();

      String firstName = "firstName";
      String lastName = "lastName";
      String username = "username";
      String password = "password";

      doSignup(firstName, lastName, username, password);

      doLogin(username, password);
    }

    private void initializeDrivers() {
      webDriver = new ChromeDriver();
      webDriverWait = new WebDriverWait(webDriver, 10);
    }

    @AfterEach
    public void afterEach() {
      doLogout();
      quitWebDriver();
    }

    private void quitWebDriver() {
      if(webDriver != null) {
        webDriver.quit();
      }
    }

    @Test
    public void create() throws InterruptedException {
      int ramdomIntHelperTest = new Random().nextInt();
      String url = "www.test" + ramdomIntHelperTest + ".com";
      String username = "username" + ramdomIntHelperTest;
      String password = "password" + ramdomIntHelperTest;

      CredentialPage credentialPage = createCredential(url, username, password);

      assertTrue(credentialPage.exists(webDriver, port, webDriverWait, url, username, password));
    }

    private CredentialPage createCredential(String url, String username, String password) {
        CredentialPage credentialPage = new CredentialPage(webDriver);
        credentialPage.create(webDriver, port, webDriverWait, url, username, password);
        return credentialPage;
    }

    @Test
    public void update() throws InterruptedException {

      int ramdomValueHelperTest = new Random().nextInt();
      String url = "www.test" + ramdomValueHelperTest + ".com";
      String username = "username" + ramdomValueHelperTest;
      String password = "password" + ramdomValueHelperTest;

      CredentialPage credentialPage = createCredential(url, username, password);

      String encryptedPassword = credentialPage.getEncryptedPwd(webDriver, port, webDriverWait, url, username);

      password = generateNewPwd();

      credentialPage.editPwd(webDriverWait, url, username, password);

      String newEncryptedPassword = credentialPage.getEncryptedPwd(webDriver, port, webDriverWait, url, username);

      assertNotEquals(encryptedPassword, newEncryptedPassword);
    }

    private static String generateNewPwd() {
      String password;
      int ramdomValueHelperTest;
      ramdomValueHelperTest = new Random().nextInt();
      password = "password" + ramdomValueHelperTest;
      return password;
    }

    @Test
    public void delete() {
      int ramdomValueHelperTest = new Random().nextInt();
      String url = "www.test" + ramdomValueHelperTest + ".com";
      String username = "username" + ramdomValueHelperTest;
      String password = "password" + ramdomValueHelperTest;

      CredentialPage credentialPage = createCredential(url, username, password);

      credentialPage.deleteCredential(webDriver, port, webDriverWait, url, username);

      assertFalse(credentialPage.exists(webDriver, port, webDriverWait, url, username, password));
    }
}
