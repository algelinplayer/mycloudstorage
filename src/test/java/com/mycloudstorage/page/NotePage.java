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

public class NotePage extends Page{

    @FindBy(id = "nav-notes-tab")
    private WebElement tab;

    @FindBy(id = "newNoteButton")
    private WebElement newNoteButton;

    @FindBy(id = "note-title")
    private WebElement titleModal;

    @FindBy(id = "note-description")
    private WebElement descriptionModal;

    @FindBy(id = "saveChangesNotesButton")
    private WebElement saveChangesNotesButton;

    @FindBy(id = "notesTable")
    private WebElement notesTable;

    public NotePage(WebDriver webDriver) {
      super();
      PageFactory.initElements(webDriver, this);
    }

    public void createNewNote(WebDriver webDriver, WebDriverWait webDriverWait, int port, String title, String description) {
      navigateToTheNotesTab(webDriver, webDriverWait, port);

      webDriverWait.until(ExpectedConditions.elementToBeClickable(newNoteButton));
      this.newNoteButton.click();

      webDriverWait.until(ExpectedConditions.visibilityOf(titleModal));
      this.titleModal.sendKeys(title);
      this.descriptionModal.sendKeys(description);
      this.saveChangesNotesButton.click();
    }

    public boolean exists(WebDriver webDriver, WebDriverWait webDriverWait, int port, String title, String description) {
      navigateToTheNotesTab(webDriver, webDriverWait, port);

      if (findNoteInTheNotesTable(webDriverWait, title, description)) return true;

      return false;
    }

  private void navigateToTheNotesTab(WebDriver webDriver, WebDriverWait webDriverWait, int port) {
    goHome(webDriver, port);
    webDriverWait.until(ExpectedConditions.elementToBeClickable(tab));
    this.tab.click();
  }

  private boolean findNoteInTheNotesTable(WebDriverWait webDriverWait, String title, String description) {
    List<WebElement> noteTableRows = notesTable.findElements(By.className("noteTableRow"));

    for (WebElement noteTableRow : noteTableRows) {
        WebElement titleElement = noteTableRow.findElement(By.className("classNoteTitle"));
        WebElement descriptionElement = noteTableRow.findElement(By.className("classNoteDescription"));

        webDriverWait.until(ExpectedConditions.visibilityOf(titleElement));
        webDriverWait.until(ExpectedConditions.visibilityOf(descriptionElement));

        if(compareTitleAndDescription(title, description, titleElement, descriptionElement)) return true;
    }
    return false;
  }

  private boolean compareTitleAndDescription(String title, String description, WebElement titleElement, WebElement descriptionElement) {
    return titleElement.getText().equals(title) && descriptionElement.getText().equals(description);
  }

  public void update(WebDriver webDriver, WebDriverWait webDriverWait, int port, String newTitle, String newDescription) {

    navigateToTheNotesTab(webDriver, webDriverWait, port);

    List<WebElement> updateButton = notesTable.findElements(By.tagName("button"));

    openUpdateModal(webDriverWait, updateButton);

    fillValuesAndSave(webDriverWait, newTitle, newDescription);

  }

  private void fillValuesAndSave(WebDriverWait webDriverWait, String newTitle, String newDescription) {
    webDriverWait.until(ExpectedConditions.visibilityOf(titleModal));
    titleModal.clear();
    titleModal.sendKeys(newTitle);
    descriptionModal.clear();
    descriptionModal.sendKeys(newDescription);
    saveChangesNotesButton.click();
  }

  private static void openUpdateModal(WebDriverWait webDriverWait, List<WebElement> updateButton) {
    webDriverWait.until(ExpectedConditions.elementToBeClickable(updateButton.get(0)));
    updateButton.get(0).click();
  }

  public void deleteNote(WebDriver webDriver, WebDriverWait webDriverWait, int port, String title, String description) {

    navigateToTheNotesTab(webDriver, webDriverWait, port);

    findNoteAndDelete(webDriverWait, title, description);

  }

  private void findNoteAndDelete(WebDriverWait webDriverWait, String title, String description) {
    List<WebElement> noteTableRows = notesTable.findElements(By.className("noteTableRow"));

    for (WebElement noteTableRow : noteTableRows) {
      WebElement titleElement = noteTableRow.findElement(By.className("classNoteTitle"));
      WebElement descriptionElement = noteTableRow.findElement(By.className("classNoteDescription"));

      webDriverWait.until(ExpectedConditions.visibilityOf(titleElement));
      webDriverWait.until(ExpectedConditions.visibilityOf(descriptionElement));

      if(compareTitleAndDescription(title, description, titleElement, descriptionElement)) {
        WebElement deleteButton = noteTableRow.findElement(By.tagName("a"));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();
      }
    }
  }

}
