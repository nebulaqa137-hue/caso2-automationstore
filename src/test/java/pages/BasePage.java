package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.ActionsHelper;
import utils.WaitHelper;

/**
 * BasePage - Clase base para Page Object Model con Page Factory
 * Todas las pages extienden de aqui
 */
public class BasePage {

    protected WebDriver driver;
    protected WaitHelper wait;
    protected ActionsHelper actions;
    protected static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WaitHelper(driver);
        this.actions = new ActionsHelper(driver);
        PageFactory.initElements(driver, this);  // Page Factory
    }
}
