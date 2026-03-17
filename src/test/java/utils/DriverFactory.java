package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * Utility - DriverFactory / WebDriver Manager
 * Gestiona la creacion y configuracion del WebDriver por browser
 * Selenium 4+ gestiona el driver automaticamente (no necesita WebDriverManager externo)
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initDriver(config.getBrowser());
        }
        return driverThread.get();
    }

    public static void initDriver(String browser) {
        log.info("Inicializando driver para browser: " + browser);
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--remote-allow-origins=*");
                // chromeOptions.addArguments("--headless=new"); // descomentar para headless
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driverThread.set(driver);
        log.info("Driver inicializado: " + browser);
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            log.info("Cerrando driver...");
            driverThread.get().quit();
            driverThread.remove();
        }
    }
}
