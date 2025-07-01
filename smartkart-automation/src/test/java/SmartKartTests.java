package smartkart; // UNCOMMENTED: This file must declare it belongs to the 'smartkart' package

import org.testng.Assert;
import org.testng.annotations.Test;
// CORRECTED: All package names are now lowercase to match your folder names
import smartkart.base.BaseTest;
import smartkart.pages.CheckoutPage;
import smartkart.pages.HomePage;
import smartkart.pages.LoginPage;
import org.openqa.selenium.By;

public class SmartKartTests extends BaseTest {

    @Test(priority = 1, description = "TC-NAV-01 & TC-CART-01: Verify dropdown and add to cart.")
    public void testDropdownAndAddToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.hoverAndClickClothing();
        Assert.assertTrue(driver.getCurrentUrl().contains("products.html?category=Clothing"), "Should navigate to Clothing category page.");

        driver.navigate().back(); // Go back to the homepage

        homePage.addFirstFourProductsToCart();
        Assert.assertEquals(homePage.getCartCount(), "4", "Cart count should be 4 after adding items.");
    }

    @Test(priority = 2, description = "TC-PAY-01: Verify successful payment from wallet.")
    public void testSuccessfulPayment() {
        // Step 1: Add items to cart
        HomePage homePage = new HomePage(driver);
        homePage.addFirstFourProductsToCart();

        // Step 2: Go to checkout
        driver.findElement(By.id("cartLinkHero")).click();

        // Assert we are on the checkout page
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout.html"), "Should be on the checkout page.");

        // Step 3: Perform payment
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        String initialBalanceStr = checkoutPage.getWalletBalanceText().replace("$", "").replace(",", "");
        double initialBalance = Double.parseDouble(initialBalanceStr);

        checkoutPage.clickBuyNow();
        checkoutPage.waitForPurchaseSuccessAndAcceptAlert();

        // Step 4: Validate
        Assert.assertTrue(driver.getCurrentUrl().contains("index.html"), "Should be redirected to homepage after purchase.");
    }
}