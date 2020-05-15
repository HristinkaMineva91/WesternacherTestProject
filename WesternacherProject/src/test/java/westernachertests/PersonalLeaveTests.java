package westernachertests;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import westernachertests.pageobject.CommonActions;
import westernachertests.pageobject.PersonalLeavesPage;

import java.text.ParseException;

import static westernachertests.pageobject.ChromeDriverUtility.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonalLeaveTests {

    private static WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        driver = setupAndGetDriver();
        CommonActions.login(driver);
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.deleteOrExportAllPersonalLeaveForms("Delete");
    }

    @Test
    public void verifyThatAllCreatedPersonalLeaveFormsCanBeDeleted() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.createMoreThanOnePersonalLeave();
        personalLeavePage.deleteOrExportAllPersonalLeaveForms("Delete");
        personalLeavePage.verifyThatHaveNoPlannedLeaves("No planned leaves");
    }

    @Test
    public void verifyThatPersonalLeaveDaysAreShownCorrectly() throws ParseException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("12/21/2020", "01/02/2021");
        personalLeavePage.verifyPersonalLeaveDays("12/21/2020", "01/02/2021");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeSaved() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("12/21/2020", "01/02/2021");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Dec 21, 2020", "Jan 2, 2021", "13", "6");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeCancelledAndEdited() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("01/05/2020", "01/10/2020");
        personalLeavePage.cancelPersonalLeaveForm();
        personalLeavePage.verifyThatPastPersonalLeaveDaysAreNotAdded("Personal leave days form is not cancelled!");
        personalLeavePage.fillPersonalLeaveForm("01/06/2020", "01/07/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Jan 6, 2020", "Jan 7, 2020", "2", "2");
    }

    @Test
    public void verifyThatPastPersonalLeaveDaysCannotBeAdded() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("01/05/202020", "01/10/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyThatPastPersonalLeaveDaysAreNotAdded("The application allows past leave days to be added!");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeEdited() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("12/21/2020", "01/02/2021");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.editPersonalLeaveForm("10/07/2020", "10/17/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Oct 7, 2020", "Oct 17, 2020", "11", "8");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeDeleted() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("10/05/2020", "10/09/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeAdded("leave successfully added.");
        personalLeavePage.deletePersonalLeaveForm();
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeDeleted("No planned leaves");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeRequested() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("10/05/2020", "10/09/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Oct 5, 2020", "Oct 9, 2020", "5", "5");
        personalLeavePage.requestPersonalLeaveForm();
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeRequested("Accepted");
        personalLeavePage.deleteOrExportAllPersonalLeaveForms("Delete");
    }

    @Test
    public void verifyThatMoreThanOnePersonalLeaveDaysCanBeRequested() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("10/05/2020", "10/09/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Oct 5, 2020", "Oct 9, 2020", "5", "5");
        personalLeavePage.requestPersonalLeaveForm();
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeRequested("Accepted");
        personalLeavePage.fillPersonalLeaveForm("11/02/2020", "11/06/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.requestAllPersonalLeaveForms("Request");
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeRequested("Accepted");
    }

    @Test
    public void verifyThatPersonalLeaveDaysCanBeExported() throws InterruptedException {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.fillPersonalLeaveForm("10/05/2020", "10/09/2020");
        personalLeavePage.savePersonalLeaveInfo();
        personalLeavePage.verifyPersonalLeaveDaysCanBeSaved("Oct 5, 2020", "Oct 9, 2020", "5", "5");
        personalLeavePage.requestPersonalLeaveForm();
        personalLeavePage.exportPersonalLeaveForm();
        personalLeavePage.verifyThatPersonalLeaveDaysFormCanBeExported("Export");
    }

    @Test
    public void verifyThatTheSiteSupportsDifferentLanguages() {
        PersonalLeavesPage personalLeavePage = new PersonalLeavesPage(driver);
        personalLeavePage.switchLanguageTo("English");
        personalLeavePage.verifyThatTheLanguageIsChanged("Personal leaves");
        personalLeavePage.switchLanguageTo("Deutsch");
        personalLeavePage.verifyThatTheLanguageIsChanged("Persönliche Blätter");
        personalLeavePage.switchLanguageTo("Bulgarian");
        personalLeavePage.verifyThatTheLanguageIsChanged("Личен отпуск");
    }

    @After
    public void tearDown() {
        quitDriver(driver);
    }
}
