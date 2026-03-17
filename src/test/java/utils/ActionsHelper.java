package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Utility - ActionsHelper
 * Scroll, hover, drag&drop, JS executor
 */
public class ActionsHelper {

    private static final Logger log = LogManager.getLogger(ActionsHelper.class);
    private WebDriver driver;
    private Actions actions;
    private JavascriptExecutor js;

    public ActionsHelper(WebDriver driver) {
        this.driver  = driver;
        this.actions = new Actions(driver);
        this.js      = (JavascriptExecutor) driver;
    }

    public void scrollToElement(WebElement element) {
        log.info("Scroll al elemento");
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToTop() {
        log.info("Scroll al top de la pagina");
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void scrollToBottom() {
        log.info("Scroll al bottom de la pagina");
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void hoverElement(WebElement element) {
        log.info("Hover sobre elemento");
        actions.moveToElement(element).perform();
    }

    public void clickWithJS(WebElement element) {
        log.info("Click via JavaScript");
        js.executeScript("arguments[0].click();", element);
    }

    public void highlightElement(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void switchToNewTab() {
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }
}
