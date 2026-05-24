package com.opencart.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * BaseTest – manages WebDriver lifecycle for all test classes.
 * All test classes extend this to inherit driver setup/teardown.
 */
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String BASE_URL = "https://awesomeqa.com/ui/index.php?route=common/home";
    protected static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    // Shared test credentials – change to match a registered account on awesomeqa.com
    protected static final String VALID_EMAIL    = "testuser@example.com";
    protected static final String VALID_PASSWORD = "Test@12345";
    protected static final String NEW_EMAIL      = "newtestuser_" + System.currentTimeMillis() + "@example.com";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        // Uncomment the next line for headless CI execution:
        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        wait   = new WebDriverWait(driver, WAIT_TIMEOUT);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            driver.quit();
        }
    }
}
