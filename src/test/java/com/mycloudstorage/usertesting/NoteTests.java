package com.mycloudstorage.usertesting;

import com.mycloudstorage.page.HomePage;
import com.mycloudstorage.page.LoginPage;
import com.mycloudstorage.page.NotePage;
import com.mycloudstorage.page.SignupPage;
import com.mycloudstorage.service.NoteService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Desativado temporariamente para depuração")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {

    private static ResourceBundle testProperties;

    private static String HTTP_LOCALHOST;
    private static String SIGNUP_URL;
    private static String LOGIN_URL;
    private static String HOME_URL;

    @LocalServerPort
    private int port;

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private NoteService noteService;

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
      goToLogin();
      LoginPage loginPage = new LoginPage(webDriver);
      loginPage.navigateAndPrepareToLogin(port, webDriver, webDriverWait);
      loginPage.login(username, password);
    }

  private void goToLogin() {
    webDriver.get(HTTP_LOCALHOST + port + LOGIN_URL);
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
    public void delete() {

        String title = "title";
        String description = "description";

        NotePage notePage = new NotePage(webDriver);
        notePage.createNewNote(webDriver, webDriverWait, port, title, description);

        assertTrue(notePage.exists(webDriver, webDriverWait, port, title, description));

        notePage.deleteNote(webDriver, webDriverWait, port, title, description);
        assertFalse(notePage.exists(webDriver, webDriverWait, port, title, description));
    }

    @Test
    public void create() {

        String title = "title";
        String description = "description";

        NotePage notePage = new NotePage(webDriver);
        notePage.createNewNote(webDriver, webDriverWait, port, title, description);

        assertTrue(notePage.exists(webDriver, webDriverWait, port, title, description));
        notePage.deleteNote(webDriver, webDriverWait, port, title, description);
    }

    @Test
    public void update() {
        String title = "title";
        String description = "description";

        NotePage notePage = new NotePage(webDriver);
        notePage.createNewNote(webDriver, webDriverWait, port, title, description);

        String titleUpdated = "title updated";
        String descriptionUpdated = "description updated";

        assertFalse(notePage.exists(webDriver, webDriverWait, port, titleUpdated, descriptionUpdated));

        notePage.update(webDriver, webDriverWait, port, titleUpdated, descriptionUpdated);
        assertTrue(notePage.exists(webDriver, webDriverWait, port, titleUpdated, descriptionUpdated));
    }
}

