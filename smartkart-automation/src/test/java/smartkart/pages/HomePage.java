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

    private By productsMenuHero = By.xpath("//section[@id='hero']//a[contains(text(), 'Products')]");
    private By clothingCategoryLinkHero = By.xpath("//section[@id='hero']//a[text()='Clothing']");
    private By featuredProductsSection = By.id("featuredProductsGrid");
    private By cartCount = By.id("cartCountHero");
    private By cartLinkHero = By.id("cartLinkHero");
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

    public void navigateToLoginPage() {
        WebElement authDiv = wait.until(ExpectedConditions.presenceOfElementLocated(authContainerHero));
        WebElement loginLink = authDiv.findElement(By.tagName("a"));
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public void logout() {
        WebElement authDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(authContainerHero));
        WebElement logoutButton = authDiv.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }

    public void addFourProductsUsingButtonXPaths() {
        addToCartByButtonXpath("/html/body/main/section[2]/div[1]/div[2]/div[2]/button[1]"); // Waffle-Knit Navy Sweater
        addToCartByButtonXpath("/html/body/main/section[2]/div[2]/div[2]/div[2]/button[1]"); // Oversized Corduroy Shirt
        addToCartByButtonXpath("/html/body/main/section[2]/div[3]/div[2]/div[2]/button[1]"); // Classic 550 Sneakers
        addToCartByButtonXpath("/html/body/main/section[2]/div[4]/div[2]/div[2]/button[1]"); // Wave Runner Air Max
    }

    private void addToCartByButtonXpath(String buttonXpath) {
        try {
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/main/section[2]/div[4]/div[2]/div[2]/button[1]")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartBtn);
            wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
            js.executeScript("arguments[0].click();", addToCartBtn);
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Add to Cart button via XPath: " + buttonXpath, e);
        }
    }



    public String getCartCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartCount));
        return driver.findElement(cartCount).getText();
    }

    public void goToCheckout() {
        WebElement cartLink = wait.until(ExpectedConditions.presenceOfElementLocated(cartLinkHero));
        js.executeScript("arguments[0].click();", cartLink);
    }
}
