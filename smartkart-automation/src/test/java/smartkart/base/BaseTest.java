package smartkart.base; // Corrected: Package must match the folder structure

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
import java.nio.file.Paths;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Corrected: Use a programmatic way to get the absolute path to your file
        // This is much more reliable than hardcoding C:\ drive paths.
        String indexPath = Paths.get("index.html").toAbsolutePath().toString();
        driver.get("file:///" + indexPath);
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

    private void takeScreenshot(String testName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            // Corrected: Saves screenshots into a folder in your project root
            String screenshotDir = Paths.get("").toAbsolutePath().toString() + "/screenshots/";
            new File(screenshotDir).mkdirs(); // Ensure the directory exists
            FileUtils.copyFile(scrFile, new File(screenshotDir + testName + ".png"));
            System.out.println("Screenshot taken for failed test: " + testName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}