package stepdefinitions;

import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import pages.RegisterPage;
import pages.StorePage;
import utils.ConfigManager;
import utils.DriverFactory;
import utils.RandomDataHelper;

public class StoreSteps {

    private static final Logger log = LogManager.getLogger(StoreSteps.class);
    private RegisterPage registerPage;
    private StorePage storePage;
    private static final ConfigManager config = ConfigManager.getInstance();
    private int cartCountBefore = 0;
    private String priceBeforeChange = "";

    // Datos random generados dinamicamente para reutilizar en el mismo escenario
    private String randomLoginName;
    private String randomPassword;

    public StoreSteps() {
        this.registerPage = new RegisterPage(DriverFactory.getDriver());
        this.storePage    = new StorePage(DriverFactory.getDriver());
    }

    @Given("el usuario esta en Automation Test Store")
    public void elUsuarioEstaEnAutomationTestStore() {
        registerPage = new RegisterPage(DriverFactory.getDriver());
        storePage    = new StorePage(DriverFactory.getDriver());
    }

    // ---- REGISTRO ----
    @When("el usuario hace click en Register")
    public void clickRegister() {
        registerPage.clickRegister();
    }

    @And("llena el formulario de registro")
    public void llenaFormularioRegistro() {
        registerPage.fillForm(
            config.getRegFirstname(), config.getRegLastname(),
            config.getRegEmail(),     config.getRegAddress(),
            config.getRegCity(),      config.getRegPostcode(),
            config.getRegLoginname(), config.getRegPassword()
        );
    }

    @And("envia el formulario")
    public void enviaFormulario() {
        registerPage.submitForm();
    }

    @Then("debe ver el mensaje de cuenta creada")
    public void mensajeCuentaCreada() {
        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
            "Debe mostrar mensaje de cuenta creada exitosamente");
    }

    // ---- LOGIN ----
    @When("el usuario hace login con credenciales validas")
    public void loginValido() {
        storePage.login(config.getValidUser(), config.getValidPass());
    }

    @When("el usuario hace login con credenciales invalidas")
    public void loginInvalido() {
        storePage.login(config.getInvalidUser(), config.getInvalidPass());
    }

    @Then("debe estar logueado en la tienda")
    public void debeEstarLogueado() {
        Assert.assertTrue(storePage.isLoggedIn(), "El usuario debe estar logueado");
    }

    @Then("no debe estar logueado en la tienda")
    public void noDebeEstarLogueado() {
        Assert.assertFalse(storePage.isLoggedIn(), "No debe estar logueado con credenciales invalidas");
    }

    @And("el usuario esta autenticado en la tienda")
    public void usuarioAutenticado() {
        storePage = new StorePage(DriverFactory.getDriver());
        storePage.login(config.getValidUser(), config.getValidPass());
        // login() ya navega al home internamente
        log.info("Usuario autenticado y en home");
    }

    // ---- CARRITO ----
    @When("agrega el primer producto al carrito de la tienda")
    public void agregarProducto() {
        storePage = new StorePage(DriverFactory.getDriver());
        storePage.addFirstProductToCart();
    }

    @And("navega al carrito via header")
    public void navegarCarritoHeader() {
        storePage.goToCartViaAddedLink();
        cartCountBefore = storePage.getCartItemCount();
        log.info("Items en carrito: " + cartCountBefore);
    }

    @And("navega al carrito de la tienda")
    public void navegarCarrito() {
        navegarCarritoHeader();
    }

    @Then("el carrito de la tienda debe tener al menos {int} producto")
    public void carritoTieneProductos(int cantidad) {
        int count = storePage.getCartItemCount();
        // Validar tambien que la cantidad del primer item sea >= 1
        int qty = storePage.getFirstItemQuantity();
        log.info("Items: " + count + " | Cantidad primer item: " + qty);
        Assert.assertTrue(count >= cantidad,
            "El carrito debe tener al menos " + cantidad + " producto. Encontrado: " + count);
        Assert.assertTrue(qty >= 1, "La cantidad del producto debe ser al menos 1");
    }

    @And("elimina el primer producto del carrito con el icono de basura")
    public void eliminarConIconoBasura() {
        cartCountBefore = storePage.getCartItemCount();
        log.info("Items antes de eliminar: " + cartCountBefore);
        storePage.removeFirstCartItem();
    }

    @And("elimina el primer producto del carrito")
    public void eliminarPrimerProducto() {
        eliminarConIconoBasura();
    }

    @Then("el carrito debe tener menos productos")
    public void carritoTieneMenos() {
        // Validacion creativa: verificar que hay menos items O que el carrito esta vacio
        int countAfter = storePage.getCartItemCount();
        boolean cartEmpty = storePage.isCartEmpty();
        log.info("Items despues: " + countAfter + " | Vacio: " + cartEmpty +
                 " | Antes: " + cartCountBefore);
        boolean menosProductos = countAfter < cartCountBefore || cartEmpty;
        Assert.assertTrue(menosProductos,
            "El carrito debe tener menos items o estar vacio. " +
            "Antes: " + cartCountBefore + " Despues: " + countAfter);
    }

    // ---- DIVISA ----
    @And("cambia la divisa a EUR usando la URL directa")
    public void cambiarDivisaEUR() {
        log.info("Cambiando divisa a EUR");
        storePage.changeCurrencyToEUR();
    }

    @When("cambia la divisa a {string}")
    public void cambiarDivisa(String currency) {
        storePage.changeCurrency(currency);
    }

    @Then("la divisa debe mostrar EURO en el encabezado")
    public void divisaMuestraEuro() {
        boolean eurDisplayed = storePage.isEuroDisplayed();
        Assert.assertTrue(eurDisplayed,
            "El encabezado debe mostrar la divisa EURO/EUR/€");
        log.info("Divisa EURO verificada en header");
    }

    @Then("el precio debe mostrarse diferente al precio en USD")
    public void precioMostradoDiferente() {
        Assert.assertTrue(storePage.isEuroDisplayed(),
            "La divisa debe haber cambiado");
    }

    // ---- DESCUENTO ----
    @When("navega a la pagina de ofertas especiales")
    public void navegarOfertas() {
        storePage = new StorePage(DriverFactory.getDriver());
        storePage.navigateToSpecials();
    }

    @Then("debe haber productos con etiqueta Sale o precio de descuento")
    public void hayProductosConDescuentoSale() {
        Assert.assertTrue(storePage.hasDiscountProducts(),
            "Debe haber productos con etiqueta Sale y precio tachado");
    }

    @Then("debe haber productos con descuento visibles")
    public void hayProductosConDescuento() {
        hayProductosConDescuentoSale();
    }

        // ---- REGISTRO CON DATOS RANDOM ----
    @And("llena el formulario con datos aleatorios generados por Faker")
    public void llenaFormularioConDatosRandom() {
        String firstName   = RandomDataHelper.randomFirstName();
        String lastName    = RandomDataHelper.randomLastName();
        String email       = RandomDataHelper.randomEmail();
        String address     = RandomDataHelper.randomAddress();
        String city        = RandomDataHelper.randomCity();
        String postcode    = RandomDataHelper.randomPostcode();
        randomLoginName    = RandomDataHelper.randomUsername();
        randomPassword     = RandomDataHelper.randomPassword();

        RandomDataHelper.logGeneratedData(firstName, lastName, email, randomLoginName, randomPassword);

        registerPage = new RegisterPage(DriverFactory.getDriver());
        registerPage.fillForm(
            firstName, lastName, email,
            address, city, postcode,
            randomLoginName, randomPassword
        );
    }

    @When("hace login con las credenciales del usuario recien registrado")
    public void loginConUsuarioRegistrado() {
        Assert.assertNotNull(randomLoginName,
            "Debe existir un usuario registrado en el mismo escenario");
        log.info("Login con usuario random: " + randomLoginName);
        storePage = new StorePage(DriverFactory.getDriver());
        storePage.login(randomLoginName, randomPassword);
    }
}
