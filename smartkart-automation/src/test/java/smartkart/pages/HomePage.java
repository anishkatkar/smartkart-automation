package smartkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private By productsMenuHero = By.xpath("//div[contains(@class, 'absolute top-0')]//a[contains(text(), 'Products')]");
    private By clothingCategoryLinkHero = By.xpath("//div[contains(@class, 'absolute top-0')]//a[text()='Clothing']");
    private By featuredProductsSection = By.id("featuredProductsGrid");
    private By cartCount = By.id("cartCountHero");
    private By cartLinkHero = By.id("cartLinkHero"); // Locator for the cart link
    private By authContainerHero = By.id("auth-container-hero");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public void hoverAndClickClothing() {
        Actions actions = new Actions(driver);
        WebElement productsLink = wait.until(ExpectedConditions.elementToBeClickable(productsMenuHero));
        actions.moveToElement(productsLink).perform();
        WebElement clothingLink = wait.until(ExpectedConditions.visibilityOfElementLocated(clothingCategoryLinkHero));
        clothingLink.click();
    }
    // NEW METHOD
    public void navigateToLoginPage() {
        // Corrected: Wait for the container to be present, then find and click the link inside it.
        WebElement authDiv = wait.until(ExpectedConditions.presenceOfElementLocated(authContainerHero));
        wait.until(ExpectedConditions.elementToBeClickable(authDiv.findElement(By.tagName("a")))).click();
    }

    // NEW METHOD
    public void logout() {
        WebElement authDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(authContainerHero));
        wait.until(ExpectedConditions.elementToBeClickable(authDiv.findElement(By.tagName("button")))).click();
    }

    public void addFirstFourProductsToCart() {
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(featuredProductsSection));
        js.executeScript("arguments[0].scrollIntoView(true);", section);
        for (int i = 1; i <= 4; i++) {
            addSingleProductToCart(i);
        }
    }

    public void addSingleProductToCart(int productIndex) {
        WebElement addToCartBtn = driver.findElement(By.xpath("(//div[@id='featuredProductsGrid']//div[contains(@class, 'product-card')])[" + productIndex + "]//button[contains(text(), 'Add to Cart')]"));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartBtn);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
        js.executeScript("arguments[0].click();", addToCartBtn);
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public String getCartCount() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartCount, "4"));
        return driver.findElement(cartCount).getText();
    }

    // --- NEW, ROBUST HELPER METHOD ---
    public void goToCheckout() {
        // This method uses the reliable JavaScript click to navigate to the checkout page.
        WebElement cartLink = wait.until(ExpectedConditions.presenceOfElementLocated(cartLinkHero));
        js.executeScript("arguments[0].click();", cartLink);
    }
}