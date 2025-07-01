package base;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // IMPORTANT: Update this with the real path to your index.html file on your machine
        // Example: "file:///C:/Users/YourUsername/IdeaProjects/smartKart/index.html"
        driver.get("file:///C:/Users/worka/path/to/your/project/smartKart/index.html");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            takeScreenshot(result.getMethod().getMethodName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    public void takeScreenshot(String testName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            // This will create a 'screenshots' folder in your smartkart-automation directory
            FileUtils.copyFile(scrFile, new File("screenshots/" + testName + ".png"));
            System.out.println("Screenshot taken for failed test: " + testName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
