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

    // Locators
    private By productsMenu = By.xpath("//nav//a[contains(text(), 'Products')]");
    private By clothingCategoryLink = By.xpath("//div[contains(@class, 'group-hover:opacity-100')]//a[contains(text(), 'Clothing')]");
    private By featuredProductsSection = By.id("featuredProductsGrid");
    private By cartCount = By.id("cartCountHero"); // Target the hero header cart

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToLoginPage() {
        // Corrected: The login link is just an 'a' tag in the hero header icons
        driver.findElement(By.cssSelector(".absolute.top-0 .fa-user-circle")).click();
    }

    public void hoverAndClickClothing() {
        Actions actions = new Actions(driver);
        WebElement productsLink = driver.findElement(productsMenu);
        actions.moveToElement(productsLink).perform();
        WebElement clothingLink = wait.until(ExpectedConditions.visibilityOfElementLocated(clothingCategoryLink));
        clothingLink.click();
    }

    public void addFirstFourProductsToCart() {
        WebElement section = driver.findElement(featuredProductsSection);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);

        for (int i = 1; i <= 4; i++) {
            // Corrected: More robust XPath and removed the non-existent alert
            WebElement addToCartBtn = driver.findElement(By.xpath("(//div[@id='featuredProductsGrid']//div[contains(@class, 'product-card')])[" + i + "]//button[contains(text(), 'Add to Cart')]"));
            wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
            // Removed: The app does not show an alert here.
        }
    }

    public String getCartCount() {
        // Corrected: Wait for the text to be "4" and then return it
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartCount, "4"));
        return driver.findElement(cartCount).getText();
    }
}