package westernachertests.pageobject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class ChromeDriverUtility {
    public static final String USERNAME = System.getProperty("username");
    public static final String PASSWORD = System.getProperty("password");

    public static WebDriver setupAndGetDriver() {
        // Setup Chrome Driver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        // Open new Chrome Browser
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Maximize the Window
        driver.manage().window().maximize();

        return driver;
    }

    public static void quitDriver(WebDriver driver) {
        // Close Chrome Browser
        if (driver != null) {
            driver.quit();
        }
    }
}




