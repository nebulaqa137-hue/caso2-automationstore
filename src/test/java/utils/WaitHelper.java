package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility - WaitHelper
 * Centralize explicit waits reutilizables
 */
public class WaitHelper {

    private static final Logger log = LogManager.getLogger(WaitHelper.class);
    private WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        this.wait = new WebDriverWait(driver,
            Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()));
    }

    public WebElement waitForVisible(By locator) {
        log.info("Esperando visible: " + locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        log.info("Esperando clickable: " + locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public boolean waitForText(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
