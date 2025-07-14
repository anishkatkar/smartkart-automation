package smartkart;

import org.openqa.selenium.JavascriptExecutor;
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

    // Use the credentials you requested
    private final String userEmail = "Anish@gmail.com";
    private final String userPassword = "Anish@1234";

    @BeforeMethod
    public void setupPages() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(priority = 1, description = "TC-AUTH-01: Verify successful registration.")
    public void testSuccessfulRegistration() {
        // Step 1: Navigate to Login Page from Home
        homePage.navigateToLoginPage();

        // Step 2: Register the new user
        loginPage.registerUser(userEmail, userPassword);

        // Step 3: Verify the success alert
        String alertText = loginPage.handleAlertAndGetText();
        Assert.assertEquals(alertText, "Registration successful! You can now log in.");
    }

    @Test(priority = 2, description = "TC-AUTH-02: Verify successful login.")
    public void testSuccessfulLogin() {
        // This test depends on the registration test having run first
        homePage.navigateToLoginPage();

        // Login with the credentials
        loginPage.loginUser(userEmail, userPassword);

        // Verify the welcome alert
        String alertText = loginPage.handleAlertAndGetText();
        Assert.assertTrue(alertText.contains("Welcome back"), "Login success alert should be shown.");

        // Verify redirection to the homepage
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("index.html"));
        Assert.assertTrue(driver.getCurrentUrl().contains("index.html"), "Should be redirected to homepage.");
    }

    @Test(priority = 3, description = "TC-PAY-01: Verify payment flow for a logged-in user.")
    public void testPaymentFlowForLoggedInUser() {
        // --- Test Setup: Login first ---
        homePage.navigateToLoginPage();
        loginPage.loginUser(userEmail, userPassword);
        loginPage.handleAlertAndGetText(); // Accept login alert
        // Now we are logged in and on the homepage

        // --- Test Execution ---
        homePage.addFirstFourProductsToCart();
        homePage.goToCheckout();

        checkoutPage.clickBuyNow();
        String alertText = checkoutPage.handleAlertAndGetText();
        Assert.assertTrue(alertText.contains("Purchase successful!"), "Payment should be successful for logged-in user.");
    }
}