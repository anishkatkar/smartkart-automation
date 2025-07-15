package smartkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By registerTab = By.id("registerTab");
    private By registerEmailInput = By.id("registerEmail");
    private By registerPasswordInput = By.id("registerPassword");
    private By registerConfirmPasswordInput = By.id("registerConfirmPassword");
    private By createAccountButton = By.cssSelector("#registerForm button");

    private By loginEmailInput = By.id("loginEmail");
    private By loginPasswordInput = By.id("loginPassword");
    private By signInButton = By.cssSelector("#loginForm button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void registerUser(String email, String password, String confirmPassword) {
        // THIS IS THE CRITICAL FIX: Click the register tab to make the form visible
        wait.until(ExpectedConditions.elementToBeClickable(registerTab)).click();

        // Now we can interact with the elements
        wait.until(ExpectedConditions.visibilityOfElementLocated(registerEmailInput)).sendKeys(email);
        driver.findElement(registerPasswordInput).sendKeys(password);
        driver.findElement(registerConfirmPasswordInput).sendKeys(password);
        driver.findElement(createAccountButton).click();
    }

    public void loginUser(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmailInput)).sendKeys(email);
        driver.findElement(loginPasswordInput).sendKeys(password);
        driver.findElement(signInButton).click();
    }

    public String handleAlertAndGetText() {
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();
        driver.switchTo().alert().accept();
        return alertText;
    }
}