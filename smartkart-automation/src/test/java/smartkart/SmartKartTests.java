package smartkart;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import smartkart.base.BaseTest;
import smartkart.pages.CheckoutPage;
import smartkart.pages.HomePage;
import smartkart.pages.LoginPage;

import java.time.Duration;

public class SmartKartTests extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;
    private CheckoutPage checkoutPage;

    // Dynamic test data (random email for each run)
    private String userEmail;
    private final String userPassword = "Prachi1404";

    @BeforeMethod
    public void setupPages() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        checkoutPage = new CheckoutPage(driver);

        // Generate a unique email ID for every test run
        userEmail = "user" + System.currentTimeMillis() + "@gmail.com";
    }

    @Test(priority = 1, description = "TC-AUTH-01: Verify successful registration.")
    public void testSuccessfulRegistration() {
        homePage.navigateToLoginPage();
        System.out.println("Registering user: " + userEmail);

        loginPage.registerUser(userEmail, userPassword, userPassword);
        String alertText = loginPage.handleAlertAndGetText();
        Assert.assertEquals(alertText, "Registration successful! You can now log in.");

        loginPage.loginUser(userEmail, userPassword);
        String alertText1 = loginPage.handleAlertAndGetText();
        System.out.println("Login alert: " + alertText1);
        Assert.assertTrue(alertText1.contains("Welcome back"), "Login success alert should be shown.");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("index.html"));
        Assert.assertTrue(driver.getCurrentUrl().contains("index.html"), "Should be redirected to homepage.");
    }

    @Test(priority = 2, description = "TC-AUTH-02: Verify successful login.")
    public void testSuccessfulLogin() {
        // First, register the user (so it's self-contained)
        homePage.navigateToLoginPage();
        System.out.println("Registering for login test: " + userEmail);

        loginPage.registerUser(userEmail, userPassword, userPassword);
        loginPage.handleAlertAndGetText(); // Registration alert

        // Now perform login
        loginPage.loginUser(userEmail, userPassword);
        String alertText = loginPage.handleAlertAndGetText();
        System.out.println("Login alert: " + alertText);
        Assert.assertTrue(alertText.contains("Welcome back"), "Login success alert should be shown.");

        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.urlContains("index.html"));
        Assert.assertTrue(driver.getCurrentUrl().contains("index.html"), "Should be redirected to homepage.");
    }

    @Test(priority = 3, description = "TC-PAY-01: Verify payment flow for a logged-in user.")
    public void testPaymentFlowForLoggedInUser() {
        // Step 1: Navigate and Register
        homePage.navigateToLoginPage();
        loginPage.registerUser(userEmail, userPassword, userPassword);
        String regAlert = loginPage.handleAlertAndGetText();
        Assert.assertTrue(regAlert.contains("Registration successful"), "Registration should succeed");

        // Step 2: Login
        loginPage.loginUser(userEmail, userPassword);
        String loginAlert = loginPage.handleAlertAndGetText();
        Assert.assertTrue(loginAlert.contains("Welcome back"), "Login should succeed");

        // Step 3: Add products to cart (defensive)
        homePage.addFourProductsUsingButtonXPaths();
        Assert.assertEquals(homePage.getCartCount(), "4", "Cart should have 4 items");

        // Step 4: Go to checkout and pay
        homePage.goToCheckout();
        checkoutPage.clickBuyNow();
        String alertText = checkoutPage.handleAlertAndGetText();

        // Step 5: Assert success
        Assert.assertTrue(alertText.contains("Purchase successful!"), "Payment should be successful for logged-in user.");
    }

}
