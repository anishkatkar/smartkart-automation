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

    private By productsMenu = By.xpath("//nav//a[contains(text(), 'Products')]");
    private By clothingCategoryLink = By.xpath("//div[contains(@class, 'group-hover:opacity-100')]//a[contains(text(), 'Clothing')]");
    private By featuredProductsSection = By.id("featuredProductsGrid");
    private By cartCount = By.id("cartCountHero"); // Target the hero header cart
    private By logoutButton = By.cssSelector("button[onclick='logout()']");
    private By loginLink = By.cssSelector("#auth-container-hero a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToLoginPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginLink)).click();
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
            WebElement addToCartBtn = driver.findElement(By.xpath("//*[@id='featuredProductsGrid']/div[" + i + "]//button[contains(text(), 'Add to Cart')]"));
            wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        }
    }

    public String getCartCount() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartCount, "4"));
        return driver.findElement(cartCount).getText();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
}