package westernachertests.pageobject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static westernachertests.pageobject.CommonActions.PAGE_URL;

public class SignUpPage {
    private WebDriver driver;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        // Navigate to Web Page: https://test.easyleave.de/
        driver.get(PAGE_URL);
        // Initialize Page Object Elements
        PageFactory.initElements(driver, this);
    }

    public void fillSignUpForm(String username, String pass) {
        email.sendKeys(username);
        password.sendKeys(pass);
        loginButton.click();
    }

    public void verifyThatTheUserIsLogged() {
        Assert.assertTrue(personalLeavePage.isDisplayed());
    }

    // Page Object Elements (locators)
    @FindBy(id = "emailAddress")
    private WebElement email;

    @FindBy(id = "mat-input-1")
    private WebElement password;

    @FindBy(id = "logButton")
    private WebElement loginButton;

    @FindBy(css = "app-leaves h2")
    private WebElement personalLeavePage;
}
