package westernachertests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import westernachertests.pageobject.SignUpPage;

import java.text.ParseException;

import static westernachertests.pageobject.ChromeDriverUtility.*;

public class SignUpTests {
    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        driver = setupAndGetDriver();
    }

    @Test
    public void verifyThatTheUserIsLogged() throws ParseException {
        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.fillSignUpForm(USERNAME, PASSWORD);
        signUpPage.verifyThatTheUserIsLogged();
    }

    @AfterClass
    public static void tearDown() {
        quitDriver(driver);
    }
}
