package smartkart.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By walletBalance = By.id("walletBalance");
    private By payWithWalletButton = By.id("buyNowBtn");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getWalletBalanceText() {
        return driver.findElement(walletBalance).getText();
    }

    public void clickPayWithWallet() {
        driver.findElement(payWithWalletButton).click();
    }

    public void waitForPurchaseSuccess() {
        wait.until(ExpectedConditions.textToBePresentInElement(payWithWalletButton, "Purchase Complete"));
    }
}
