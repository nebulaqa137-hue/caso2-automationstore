package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility - ScreenshotHelper
 * Captura screenshots en fallos y pasos clave
 */
public class ScreenshotHelper {

    private static final Logger log = LogManager.getLogger(ScreenshotHelper.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    public static String takeScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(fileName);
            FileUtils.copyFile(src, dest);
            log.info("Screenshot guardado: " + fileName);
            return dest.getAbsolutePath();
        } catch (IOException e) {
            log.error("Error al tomar screenshot: " + e.getMessage());
            return null;
        }
    }

    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Error al tomar screenshot como bytes: " + e.getMessage());
            return new byte[0];
        }
    }
}
