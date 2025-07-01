package smartkart.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    private By registerTab = By.id("registerTab");
    private By registerEmailInput = By.id("registerEmail");
    private By registerPasswordInput = By.id("registerPassword");
    private By registerConfirmPasswordInput = By.id("registerConfirmPassword");
    private By createAccountButton = By.cssSelector("#registerForm button[type='submit']");
    private By loginEmailInput = By.id("loginEmail");
    private By loginPasswordInput = By.id("loginPassword");
    private By signInButton = By.cssSelector("#loginForm button[type='submit']");

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
