package smartkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    // Corrected: Locators now match the actual HTML structure
    private By registerTab = By.xpath("//button[text()='Register']");
    private By registerEmailInput = By.cssSelector("#registerForm input[placeholder='you@example.com']");
    private By registerPasswordInput = By.cssSelector("#registerForm input[placeholder='••••••••']:nth-of-type(1)");
    private By registerConfirmPasswordInput = By.cssSelector("#registerForm input[placeholder='••••••••']:nth-of-type(2)");
    private By createAccountButton = By.cssSelector("#registerForm button");

    private By loginEmailInput = By.cssSelector("#loginForm input[placeholder='you@example.com']");
    private By loginPasswordInput = By.cssSelector("#loginForm input[placeholder='••••••••']");
    private By signInButton = By.cssSelector("#loginForm button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void registerUser(String email, String password) {
        driver.findElement(registerTab).click();
        driver.findElement(registerEmailInput).sendKeys(email);
        driver.findElement(registerPasswordInput).sendKeys(password);
        driver.findElement(registerConfirmPasswordInput).sendKeys(password);
        driver.findElement(createAccountButton).click();
    }

    public void loginUser(String email, String password) {
        driver.findElement(loginEmailInput).sendKeys(email);
        driver.findElement(loginPasswordInput).sendKeys(password);
        driver.findElement(signInButton).click();
    }
}