package westernachertests.pageobject;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PersonalLeavesPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    public PersonalLeavesPage(WebDriver driver) {
        this.driver = driver;
        // Wait for WebElement visibility
        wait = new WebDriverWait(driver, 50);
        // Initialize Page Object Elements
        PageFactory.initElements(driver, this);
    }

    public void fillPersonalLeaveForm(String startDate, String endDate) {
        clickOnElement(addButton);
        fillPersonalLeaveForm(fromDate, toDate, startDate, endDate);
    }

    public void editPersonalLeaveForm(String startDate, String endDate) {
        clickOnElement(editButton);
        fillPersonalLeaveForm(fromDate, toDate, startDate, endDate);
    }

    public void deletePersonalLeaveForm() {
        clickOnElement(deleteButton);
        clickOnElement(addPersonalLeaveButton);
        new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(String.valueOf(modalDialog))));
    }

    public void requestPersonalLeaveForm() {
        clickOnElement(requestButton);
        wait.until(ExpectedConditions.visibilityOf(actualRequestMessage));
    }

    public void exportPersonalLeaveForm() {
        clickOnElement(exportButton);
    }

    public void savePersonalLeaveInfo() throws InterruptedException {
        clickOnElement(addPersonalLeaveButton);
        wait.until(ExpectedConditions.visibilityOf(daysOffCell));
        Thread.sleep(3000);
    }

    public void cancelPersonalLeaveForm() {
        clickOnElement(cancelPersonalLeaveButton);
        wait.until(ExpectedConditions.visibilityOf(table));
    }

    public void switchLanguageTo(String language) {
        clickOnElement(wait.until(ExpectedConditions.visibilityOf(languageArrow)));
        if (language == "English") {
            clickOnElement(wait.until(ExpectedConditions.visibilityOf(englishLanguageOption)));
        } else if (language == "Deutsch") {
            clickOnElement(wait.until(ExpectedConditions.visibilityOf(deutschLanguageOption)));
        } else if (language == "Bulgarian") {
            clickOnElement(wait.until(ExpectedConditions.visibilityOf(bulgarianLanguageOption)));
        }
    }

    public void verifyPersonalLeaveDays(String startDate, String endDate) throws ParseException {
        Assert.assertEquals("Days: " + getDateDifferenceInDays(startDate, endDate), days.getText());
        Assert.assertEquals("Days off: " + getDateDifferenceInDays(startDate, endDate), daysOff.getText());
    }

    public void verifyPersonalLeaveDaysCanBeSaved(String startDate, String endDate, String days, String daysOff) {
        Assert.assertEquals(startDate, wait.until(ExpectedConditions.visibilityOf(fromDateCell)).getText());
        Assert.assertEquals(endDate, wait.until(ExpectedConditions.visibilityOf(toDateCell)).getText());
        Assert.assertEquals(days, wait.until(ExpectedConditions.visibilityOf(daysCell)).getText());
        Assert.assertEquals(daysOff, wait.until(ExpectedConditions.visibilityOf(daysOffCell)).getText());
    }

    public void verifyThatPastPersonalLeaveDaysAreNotAdded(String message) {
        Assert.assertFalse(message, fromDateCell.isDisplayed());
        Assert.assertFalse(message, toDateCell.isDisplayed());
        Assert.assertFalse(message, daysCell.isDisplayed());
        Assert.assertFalse(message, daysOffCell.isDisplayed());
    }

    public void verifyThatPersonalLeaveDaysFormCanBeAdded(String expectedAddMessage) {
        wait.until(ExpectedConditions.visibilityOf(actualMessage));
        Assert.assertTrue(actualMessage.getText().startsWith(expectedAddMessage));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(String.valueOf(actualMessage))));
    }

    public void verifyThatPersonalLeaveDaysFormCanBeDeleted(String expectedDeleteMessage) {
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(actualMessage)).isDisplayed());
        Assert.assertEquals(expectedDeleteMessage, noPlannedLeavesText.getText());
    }

    public void verifyThatPersonalLeaveDaysFormCanBeRequested(String expectedRequestMessage) {
        Assert.assertEquals(expectedRequestMessage, wait.until(ExpectedConditions.visibilityOf(actualRequestMessage)).getText());
    }

    public void verifyThatPersonalLeaveDaysFormCanBeExported(String expectedText) {
        Assert.assertEquals(expectedText, actualExportPage.getText());
        Assert.assertEquals(expectedText, addPersonalLeaveButton.getText());
    }

    public void verifyThatTheLanguageIsChanged(String expectedText) {
        Assert.assertEquals(expectedText, wait.until(ExpectedConditions.visibilityOf(actualPersonalLeaveText)).getText());
    }

    public void verifyThatHaveNoPlannedLeaves(String expectedText) {
        Assert.assertEquals(expectedText, wait.until(ExpectedConditions.visibilityOf(noPlannedLeavesText)).getText());
    }

    private long getDateDifferenceInDays(String date1, String date2) {
        LocalDate d1 = parseDate(date1);
        LocalDate d2 = parseDate(date2);

        return ChronoUnit.DAYS.between(d1, d2) + 1;
    }

    private static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public void fillPersonalLeaveForm(WebElement fromDate, WebElement toDate, String startDate, String endDate) {
        fromDate.sendKeys(Keys.CONTROL + "a");
        fromDate.sendKeys(Keys.DELETE);
        fromDate.sendKeys(startDate);
        toDate.sendKeys(Keys.CONTROL + "a");
        toDate.sendKeys(Keys.DELETE);
        toDate.sendKeys(endDate);
        toDate.sendKeys(Keys.ENTER);
    }

    public void clickOnElement(WebElement button) {
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public void requestAllPersonalLeaveForms(String textOfButton) {
        rows.stream().skip(1).forEach(row -> {
            try {
                WebElement button = row.findElement(By.xpath(".//td[6]/button[text()='" + textOfButton + "']"));
                clickOnElement(wait.until(ExpectedConditions.elementToBeClickable(button)));
                Thread.sleep(6000);
            } catch (NoSuchElementException ex) {
                System.out.println("No planned leaves");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
    }

    public void deleteOrExportAllPersonalLeaveForms(String textOfButton) {
        rows.stream().skip(1).forEach(row -> {
            try {
                Thread.sleep(3000);
                WebElement button = row.findElement(By.xpath(".//td[6]/button[text()='" + textOfButton + "']"));
                clickOnElement(wait.until(ExpectedConditions.elementToBeClickable(button)));
                clickOnElement(wait.until(ExpectedConditions.elementToBeClickable(addPersonalLeaveButton)));
                Thread.sleep(6000);
            } catch (NoSuchElementException ex) {
                System.out.println("No planned leaves");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
    }

    public void createMoreThanOnePersonalLeave() throws InterruptedException {
        String[] dates = new String[]{"12/21/2020", "01/02/2021", "01/06/2020", "01/07/2020"};
        for (int i = 0; i < dates.length - 1; i++) {
            fillPersonalLeaveForm(dates[i], dates[i + 1]);
            savePersonalLeaveInfo();
            Thread.sleep(6000);
        }
    }

    @FindBy(xpath = "html/body/app-root/app-home/div/app-leaves/table/tbody/tr")
    private List<WebElement> rows;

    @FindBy(css = "app-leaves button.btn.btn-primary")
    private WebElement addButton;

    @FindBy(id = "fromDate")
    private WebElement fromDate;

    @FindBy(id = "toDate")
    private WebElement toDate;

    @FindBy(css = "form > div:nth-child(2)")
    private WebElement days;

    @FindBy(css = "form > div:nth-child(3)")
    private WebElement daysOff;

    @FindBy(css = "mat-dialog-actions .btn.btn-primary")
    private WebElement addPersonalLeaveButton;

    @FindBy(css = "mat-dialog-actions > button:nth-child(2)")
    private WebElement cancelPersonalLeaveButton;

    @FindBy(css = ".fullwidth.table.table-bordered")
    private WebElement table;

    @FindBy(css = ".ng-star-inserted > td:nth-child(1)")
    private WebElement fromDateCell;

    @FindBy(css = "tr.ng-star-inserted > td:nth-child(2)")
    private WebElement toDateCell;

    @FindBy(css = "tr.ng-star-inserted > td:nth-child(3)")
    private WebElement daysCell;

    @FindBy(css = "tr.ng-star-inserted > td:nth-child(4)")
    private WebElement daysOffCell;

    @FindBy(css = "td:nth-child(6) > button:nth-child(1)")
    private WebElement editButton;

    @FindBy(css = "tr.ng-star-inserted > td:nth-child(6) > button.btn.btn-default")
    private WebElement deleteButton;

    @FindBy(css = "app-home .ng-star-inserted")
    private WebElement actualMessage;

    @FindBy(css = "#mat-dialog-0")
    private WebElement modalDialog;

    @FindBy(css = "tr:nth-child(2) > td:nth-child(6) > button:nth-child(2)")
    private WebElement requestButton;

    @FindBy(css = ".accepted")
    private WebElement actualRequestMessage;

    @FindBy(css = "tr.ng-star-inserted > td:nth-child(6) > button:nth-child(1)")
    private WebElement exportButton;

    @FindBy(css = ".md-select")
    private WebElement dropdown;

    @FindBy(css = ".mat-select-arrow")
    private WebElement languageArrow;

    @FindBy(id = "mat-option-0")
    private WebElement englishLanguageOption;

    @FindBy(id = "mat-option-1")
    private WebElement deutschLanguageOption;

    @FindBy(id = "mat-option-2")
    private WebElement bulgarianLanguageOption;

    @FindBy(css = "tr.empty.fullwidth.ng-star-inserted > td")
    private WebElement noPlannedLeavesText;

    @FindBy(css = ".mat-dialog-title")
    private WebElement actualExportPage;

    @FindBy(css = "app-leaves > h2")
    private WebElement actualPersonalLeaveText;
}
