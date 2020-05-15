package westernachertests.pageobject;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

import static westernachertests.pageobject.ChromeDriverUtility.PASSWORD;
import static westernachertests.pageobject.ChromeDriverUtility.USERNAME;

public class CommonActions {
    public static final String PAGE_URL = "https://test.easyleave.de/";

    public static void login(WebDriver driver) throws InterruptedException {
        if (USERNAME == null || PASSWORD == null) {
            throw new InvalidArgumentException("Missing 'username' or 'password' system properties");
        }
        SignUpPage page = new SignUpPage(driver);
        page.fillSignUpForm(USERNAME, PASSWORD);
        Thread.sleep(6000);
    }
}
