import base.BaseTest;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class SmartKartTests extends BaseTest {

    @Test(priority = 1, description = "TC-REG-01: Verify successful user registration.")
    public void testSuccessfulRegistration() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateToLoginPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.registerUser("testuser@example.com", "password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        Assert.assertEquals(alertText, "Registration successful! You can now log in.");
        driver.switchTo().alert().accept();
    }

    @Test(priority = 2, description = "TC-LOGIN-01 & TC-LOGIN-02: Verify login and redirect.")
    public void testSuccessfulLoginAndRedirect() {
        driver.get("file:///C:/Users/worka/path/to/your/project/smartKart/login.html");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser("testuser@example.com", "password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        Assert.assertTrue(alertText.contains("Welcome back"));
        driver.switchTo().alert().accept();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.endsWith("index.html"), "Should be redirected to the homepage.");
    }

    @Test(priority = 3, description = "TC-NAV-01 & TC-CART-01: Verify dropdown and add to cart.")
    public void testDropdownAndAddToCart() {
        HomePage homePage = new HomePage(driver);

        homePage.hoverAndClickClothing();
        Assert.assertTrue(driver.getCurrentUrl().contains("products.html?category=Clothing"));

        driver.navigate().back();

        homePage.addFirstFourProductsToCart();
        Assert.assertEquals(homePage.getCartCount(), "4");
    }

    @Test(priority = 4, description = "TC-PAY-01: Verify successful payment from wallet.")
    public void testSuccessfulPayment() {
        // Step 1: Login
        driver.get("file:///C:/Users/worka/path/to/your/project/smartKart/login.html");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser("testuser@example.com", "password123");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent()).accept();

        // Step 2: Add an item and go to checkout
        HomePage homePage = new HomePage(driver);
        homePage.addFirstFourProductsToCart(); // Adds 4 items
        driver.findElement(By.id("cartLinkHero")).click();

        // Step 3: Perform payment
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        String initialBalanceStr = checkoutPage.getWalletBalanceText().replace("$", "").replace(",", "");
        double initialBalance = Double.parseDouble(initialBalanceStr);

        checkoutPage.clickPayWithWallet();
        checkoutPage.waitForPurchaseSuccess();

        // Step 4: Validate
        String finalBalanceStr = checkoutPage.getWalletBalanceText().replace("$", "").replace(",", "");
        double finalBalance = Double.parseDouble(finalBalanceStr);

        Assert.assertTrue(finalBalance < initialBalance, "Wallet balance should decrease.");
    }

    @Test(priority = 5, description = "TC-LOGOUT-01: Verify logout functionality.")
    public void testLogout() {
        driver.get("file:///C:/Users/worka/path/to/your/project/smartKart/login.html");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser("testuser@example.com", "password123");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent()).accept();

        HomePage homePage = new HomePage(driver);
        homePage.logout();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.endsWith("login.html"), "Should be redirected to the login page after logout.");
    }
}