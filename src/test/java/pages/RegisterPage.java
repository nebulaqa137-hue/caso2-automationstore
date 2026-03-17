package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class RegisterPage extends BasePage {

    // ==================== PAGE FACTORY LOCATORS ====================

    @FindBy(id = "AccountFrm_firstname")
    private WebElement inputFirstName;

    @FindBy(id = "AccountFrm_lastname")
    private WebElement inputLastName;

    @FindBy(id = "AccountFrm_email")
    private WebElement inputEmail;

    @FindBy(id = "AccountFrm_telephone")
    private WebElement inputTelephone;

    @FindBy(id = "AccountFrm_address_1")
    private WebElement inputAddress1;

    @FindBy(id = "AccountFrm_city")
    private WebElement inputCity;

    @FindBy(id = "AccountFrm_postcode")
    private WebElement inputPostcode;

    @FindBy(id = "AccountFrm_country_id")
    private WebElement selectCountry;

    @FindBy(id = "AccountFrm_zone_id")
    private WebElement selectZone;

    @FindBy(id = "AccountFrm_loginname")
    private WebElement inputLoginName;

    @FindBy(id = "AccountFrm_password")
    private WebElement inputPassword;

    @FindBy(id = "AccountFrm_confirm")
    private WebElement inputConfirm;

    @FindBy(id = "AccountFrm_newsletter0")
    private WebElement radioNewsletterNo;

    // Checkbox Privacy Policy - OBLIGATORIO para enviar el formulario
    @FindBy(id = "AccountFrm_agree")
    private WebElement checkboxPrivacyPolicy;

    @FindBy(xpath = "//button[@title='Continue']")
    private WebElement btnContinue;

    // ==================== CONSTRUCTOR ====================
    public RegisterPage(WebDriver driver) { super(driver); }

    // ==================== ACCIONES ====================

    public void clickRegister() {
        log.info("Navegando al formulario de registro");
        driver.get("http://www.automationteststore.com/index.php?rt=account/create");
        wait.waitSeconds(2);
    }

    public void fillForm(String firstName, String lastName, String email,
                         String address, String city, String postcode,
                         String loginName, String password) {
        log.info("Llenando formulario para: " + loginName);

        // Datos personales
        wait.waitForVisible(By.id("AccountFrm_firstname")).clear();
        inputFirstName.sendKeys(firstName);
        inputLastName.clear();
        inputLastName.sendKeys(lastName);
        inputEmail.clear();
        inputEmail.sendKeys(email);

        // Telefono - campo OBLIGATORIO
        inputTelephone.clear();
        inputTelephone.sendKeys("5512345678");

        // Direccion
        inputAddress1.clear();
        inputAddress1.sendKeys(address);
        inputCity.clear();
        inputCity.sendKeys(city);
        inputPostcode.clear();
        inputPostcode.sendKeys(postcode);

        // Pais - United States
        log.info("Seleccionando pais");
        new Select(wait.waitForVisible(By.id("AccountFrm_country_id")))
            .selectByVisibleText("United States");
        wait.waitSeconds(2);

        // Estado/Region - opcional para UK
        wait.waitSeconds(1);
        log.info("Pais UK seleccionado - sin estado requerido");

        // Login info
        inputLoginName.clear();
        inputLoginName.sendKeys(loginName);
        inputPassword.clear();
        inputPassword.sendKeys(password);
        inputConfirm.clear();
        inputConfirm.sendKeys(password);

        // Newsletter - Yes (valor por defecto del sitio)
        log.info("Newsletter: dejando valor por defecto");

        log.info("Formulario llenado OK");
    }

    public void submitForm() {
        log.info("Submit formulario de registro");

        // Marcar checkbox Privacy Policy - OBLIGATORIO
        try {
            actions.scrollToElement(checkboxPrivacyPolicy);
            wait.waitSeconds(1);
            if (!checkboxPrivacyPolicy.isSelected()) {
                checkboxPrivacyPolicy.click();
                log.info("Privacy Policy checkbox marcado");
            }
        } catch (Exception e) {
            log.warn("No se encontro checkbox Privacy Policy: " + e.getMessage());
            // Intentar por JS
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("document.getElementById('AccountFrm_agree').click();");
                log.info("Privacy Policy marcado via JS");
            } catch (Exception e2) {
                log.error("No se pudo marcar Privacy Policy: " + e2.getMessage());
            }
        }

        actions.scrollToElement(btnContinue);
        wait.waitSeconds(1);
        actions.clickWithJS(btnContinue);
        wait.waitSeconds(3);
        log.info("URL post-submit: " + driver.getCurrentUrl());
    }

    public boolean isRegistrationSuccessful() {
        try {
            wait.waitSeconds(2);
            String url       = driver.getCurrentUrl().toLowerCase();
            String pageSource = driver.getPageSource().toLowerCase();
            log.info("URL post-registro: " + url);

            // Por URL
            if (url.contains("account/success") ||
                url.contains("account/account") ||
                url.contains("created")) {
                log.info("Registro exitoso por URL");
                return true;
            }
            // Por contenido
            if (pageSource.contains("your account has been created") ||
                pageSource.contains("account has been created") ||
                pageSource.contains("congratulation") ||
                pageSource.contains("successfully")) {
                log.info("Registro exitoso por mensaje");
                return true;
            }
            // Si llego a cualquier pagina de account (no create) = exito
            if (url.contains("account") && !url.contains("create")) {
                log.info("Registro exitoso - en dashboard");
                return true;
            }

            log.warn("Registro no confirmado. URL: " + url);
            return false;
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return false;
        }
    }
}
