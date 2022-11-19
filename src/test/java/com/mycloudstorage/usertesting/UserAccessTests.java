package com.mycloudstorage.usertesting;

import com.mycloudstorage.page.HomePage;
import com.mycloudstorage.page.LoginPage;
import com.mycloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAccessTests {

    private static ResourceBundle testProperties;

    private static String HTTP_LOCALHOST;
    private static String SIGNUP_URL;
    private static String LOGIN_URL;
    private static String HOME_URL;


    @LocalServerPort
    private int port;

    private WebDriver webDriver;
    private WebDriverWait waitWebDriver;

    @BeforeAll
    static void beforeAll() {
      WebDriverManager.chromedriver().setup();
      testProperties = ResourceBundle.getBundle("test");
      HTTP_LOCALHOST = testProperties.getString("HTTP_LOCALHOST");
      LOGIN_URL = testProperties.getString("LOGIN_URL");
      HOME_URL = testProperties.getString("HOME_URL");
      SIGNUP_URL = testProperties.getString("SIGNUP_URL");
    }

    @BeforeEach
    public void beforeEach() {
      initializeDrivers();
    }

    private void initializeDrivers() {
      webDriver = new ChromeDriver();
      waitWebDriver = new WebDriverWait(webDriver, 10);
    }

    @AfterEach
    public void afterEach() {
      quitWebDriver();
    }

    private void quitWebDriver() {
      if (webDriver != null) {
          webDriver.quit();
      }
    }

    @Test
    public void unauthorized() {

      tryHomeNotAuthenticated();

      tryLoginNotAuthenticated();

      trySignupNotAuthenticated();

    }

    private void trySignupNotAuthenticated() {
      goToSignup();
      assertEquals(HTTP_LOCALHOST + port + SIGNUP_URL, webDriver.getCurrentUrl());
    }

  private void goToSignup() {
    webDriver.get(HTTP_LOCALHOST + port + SIGNUP_URL);
  }

  private void tryLoginNotAuthenticated() {
    goToLogin();
    assertEquals(HTTP_LOCALHOST + port + LOGIN_URL, webDriver.getCurrentUrl());
    }

  private void goToLogin() {
    webDriver.get(HTTP_LOCALHOST + port + LOGIN_URL);
  }

  private void tryHomeNotAuthenticated() {
    goHome();
    assertEquals(HTTP_LOCALHOST + port + LOGIN_URL, webDriver.getCurrentUrl());
  }

  private void goHome() {
    webDriver.get(HTTP_LOCALHOST + port + HOME_URL);
  }

  @Test
    public void testSignUpAndLoginAndLogout() {
        String firstName = "firstName";
        String lastName = "lastName";
        String username = "username";
        String password = "password";

        doSignup(firstName, lastName, username, password);

        doLogin(username, password);
        assertEquals(HTTP_LOCALHOST + port + HOME_URL, webDriver.getCurrentUrl());

        doLogout();

        goToHome(HOME_URL);
        assertEquals(HTTP_LOCALHOST + port + LOGIN_URL, webDriver.getCurrentUrl());

    }

    private void goToHome(String homeUrl) {
      webDriver.get(HTTP_LOCALHOST + port + homeUrl);
    }

    private void doLogout() {
      HomePage homePage = new HomePage(webDriver);
      homePage.prepareToLogout(waitWebDriver);
      homePage.logout();
    }

    private void doLogin(String username, String password) {
      goToLogin();
      LoginPage loginPage = new LoginPage(webDriver);
      loginPage.navigateAndPrepareToLogin(port, webDriver, waitWebDriver );
      loginPage.login(username, password);
    }

    private void doSignup(String firstName, String lastName, String username, String password) {
      SignupPage signupPage = new SignupPage(webDriver);
      signupPage.navigateAndPrepareToSignup(port, webDriver, waitWebDriver );
      signupPage.signUp(firstName, lastName, username, password);
    }

}
