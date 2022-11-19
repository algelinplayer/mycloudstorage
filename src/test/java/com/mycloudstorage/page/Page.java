package com.mycloudstorage.page;

import org.openqa.selenium.WebDriver;

import java.util.ResourceBundle;

public class Page {
  protected ResourceBundle testProperties;
  protected String HTTP_LOCALHOST;
  protected String HOME_URL;

  public Page() {
    testProperties = ResourceBundle.getBundle("test");
    HTTP_LOCALHOST = testProperties.getString("HTTP_LOCALHOST");
    HOME_URL = testProperties.getString("HOME_URL");
  }

  protected void goHome(WebDriver webDriver, int port) {
    webDriver.get(HTTP_LOCALHOST + port + HOME_URL);
  }
}
