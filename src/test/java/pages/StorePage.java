package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class StorePage extends BasePage {

    private static final String BASE        = "https://automationteststore.com";
    private static final String HOME_URL    = BASE + "/";
    private static final String LOGIN_URL   = BASE + "/index.php?rt=account/login";
    private static final String CART_URL    = BASE + "/index.php?rt=checkout/cart";
    private static final String SPECIAL_URL = BASE + "/index.php?rt=product/special";
    // URL exacta del HTML para cambiar divisa a EUR
    private static final String EUR_URL     = BASE + "/index.php?rt=checkout/cart&currency=EUR";

    // ===== LOGIN =====
    @FindBy(id = "loginFrm_loginname")
    private WebElement inputLoginName;

    @FindBy(id = "loginFrm_password")
    private WebElement inputLoginPass;

    public StorePage(WebDriver driver) { super(driver); }

    // ===== LOGIN =====
    public void login(String user, String pass) {
        log.info("Login: " + user);
        driver.get(LOGIN_URL);
        wait.waitSeconds(2);

        wait.waitForVisible(By.id("loginFrm_loginname")).clear();
        inputLoginName.sendKeys(user);
        inputLoginPass.clear();
        inputLoginPass.sendKeys(pass);

        // Boton Login del lado Returning Customer
        List<WebElement> btns = driver.findElements(By.cssSelector("button"));
        for (WebElement btn : btns) {
            if (btn.getText().trim().equalsIgnoreCase("Login")) {
                log.info("Click en boton Login");
                actions.clickWithJS(btn);
                break;
            }
        }
        wait.waitSeconds(3);
        log.info("URL post-login: " + driver.getCurrentUrl());
    }

    public boolean isLoggedIn() {
        boolean ok = driver.getPageSource().contains("Welcome back");
        log.info("Logueado: " + ok);
        return ok;
    }

    // ===== ADD TO CART =====
    // El boton es: <a data-id="52" href="#" class="productcart" title="Add to Cart">
    // Hace llamada AJAX a addToCart con el data-id
    // Despues del click aparece el icono del carrito:
    // <a href="...?rt=checkout/cart" title="Added to cart"><i class="fa fa-shopping-cart fa-fw"></i></a>
    // Contador estatico para rotar productos en cada escenario
    private static int productIndex = 0;

    public void addFirstProductToCart() {
        log.info("Navegando al home para agregar producto");
        driver.get(HOME_URL);
        wait.waitSeconds(3);

        // Buscar todos los botones AJAX (href="#") - hay varios productos en el home
        List<WebElement> cartBtns = wait.waitForPresence(
            By.cssSelector("a.productcart[href='#']"));
        log.info("Botones Add to Cart encontrados: " + cartBtns.size());

        if (!cartBtns.isEmpty()) {
            // Rotar el indice para seleccionar un producto diferente en cada escenario
            int idx = productIndex % cartBtns.size();
            productIndex++; // Incrementar para el siguiente escenario
            WebElement btn = cartBtns.get(idx);
            String dataId = btn.getAttribute("data-id");
            log.info("Seleccionando producto indice=" + idx + " data-id=" + dataId);
            actions.scrollToElement(btn);
            wait.waitSeconds(1);
            btn.click(); // Click real - dispara el AJAX
            wait.waitSeconds(3);
            log.info("Producto " + dataId + " agregado al carrito");
        }
    }

    // ===== NAVEGAR AL CARRITO VIA HEADER =====
    // Despues del addToCart aparece: <a href="...?rt=checkout/cart" title="Added to cart">
    // con el icono fa-shopping-cart
    public void goToCartViaAddedLink() {
        log.info("Navegando al carrito via link 'Added to cart'");
        try {
            // Buscar el link que aparece despues de agregar al carrito
            WebElement addedLink = wait.waitForClickable(
                By.cssSelector("a[title='Added to cart']"));
            log.info("Click en 'Added to cart' link");
            addedLink.click();
            wait.waitSeconds(3);
        } catch (Exception e) {
            // Fallback: navegar directo al CART
            log.warn("No aparecio 'Added to cart', navegando directo: " + CART_URL);
            driver.get(CART_URL);
            wait.waitSeconds(3);
        }
        log.info("En carrito: " + driver.getCurrentUrl());
    }

    // ===== VALIDAR CANTIDAD EN CARRITO =====
    // Del HTML: <input type="text" name="quantity[51]" id="cart_quantity51" value="1">
    public int getCartItemCount() {
        try {
            // Contar inputs de cantidad - uno por cada producto
            List<WebElement> quantityInputs = driver.findElements(
                By.cssSelector("input[name^='quantity[']"));
            log.info("Inputs de cantidad encontrados: " + quantityInputs.size());
            return quantityInputs.size();
        } catch (Exception e) {
            log.warn("Error contando items: " + e.getMessage());
            return 0;
        }
    }

    public int getFirstItemQuantity() {
        try {
            // Obtener el value del primer input de cantidad
            List<WebElement> inputs = driver.findElements(
                By.cssSelector("input[name^='quantity[']"));
            if (!inputs.isEmpty()) {
                int qty = Integer.parseInt(inputs.get(0).getAttribute("value"));
                log.info("Cantidad del primer item: " + qty);
                return qty;
            }
            return 0;
        } catch (Exception e) { return 0; }
    }

    // ===== ELIMINAR ITEM =====
    // Del HTML: <a href="...?rt=checkout/cart&remove=51" class="btn btn-sm btn-default">
    //           <i class="fa fa-trash-o fa-fw"></i></a>
    public void removeFirstCartItem() {
        log.info("Eliminando primer item con icono de basura");
        try {
            // Buscar el boton con fa-trash-o - es un link con clase btn btn-sm btn-default
            List<WebElement> trashBtns = wait.waitForPresence(
                By.cssSelector("a.btn.btn-sm.btn-default"));
            log.info("Botones de eliminar (trash) encontrados: " + trashBtns.size());

            if (!trashBtns.isEmpty()) {
                String removeUrl = trashBtns.get(0).getAttribute("href");
                log.info("URL de eliminar: " + removeUrl);
                // Navegar a la URL de eliminar directamente (mas estable que click)
                driver.get(removeUrl);
                wait.waitSeconds(3);
                log.info("Item eliminado. URL: " + driver.getCurrentUrl());
            }
        } catch (Exception e) {
            log.error("Error eliminando item: " + e.getMessage());
        }
    }

    // ===== VALIDACION CREATIVA DEL CARRITO VACIO =====
    public boolean isCartEmpty() {
        // Verificar que no hay inputs de cantidad (sin productos)
        List<WebElement> inputs = driver.findElements(
            By.cssSelector("input[name^='quantity[']"));
        // O verificar mensaje de carrito vacio
        boolean emptyMsg = driver.getPageSource().toLowerCase().contains("empty") ||
                           driver.getPageSource().toLowerCase().contains("no items");
        boolean noItems  = inputs.isEmpty();
        log.info("Carrito vacio - noItems: " + noItems + " | emptyMsg: " + emptyMsg);
        return noItems || emptyMsg;
    }

    public String getCartTotal() {
        try {
            WebElement total = driver.findElement(By.cssSelector(".totalamout"));
            return total.getText().trim();
        } catch (Exception e) { return ""; }
    }

    // ===== DIVISA =====
    // URL exacta del HTML: href="...?rt=checkout/cart&currency=EUR"
    public void changeCurrencyToEUR() {
        log.info("Cambiando divisa a EUR via URL: " + EUR_URL);
        driver.get(EUR_URL);
        wait.waitSeconds(3);
        log.info("URL post-cambio: " + driver.getCurrentUrl());
    }

    public void changeCurrency(String code) {
        String url = BASE + "/index.php?rt=checkout/cart&currency=" + code.toUpperCase();
        log.info("Cambiando divisa via URL: " + url);
        driver.get(url);
        wait.waitSeconds(3);
    }

    // Verificar que el encabezado muestra EURO
    // Del HTML: <span class="label label-orange font14">$</span> US Dollar
    // Cuando es EUR mostrara: € Euro
    public boolean isEuroDisplayed() {
        try {
            // Buscar el texto de la divisa en el header
            WebElement currencyLabel = wait.waitForVisible(
                By.cssSelector(".nav.language .dropdown-toggle"));
            String text = currencyLabel.getText();
            log.info("Divisa en header: " + text);
            return text.contains("Euro") || text.contains("EUR") || text.contains("€");
        } catch (Exception e) {
            // Verificar en el source de la pagina
            boolean inSource = driver.getPageSource().contains("Euro") ||
                               driver.getPageSource().contains("€");
            log.info("Euro en pagina source: " + inSource);
            return inSource;
        }
    }

    // ===== DESCUENTO =====
    public void navigateToSpecials() {
        log.info("Navegando a Specials: " + SPECIAL_URL);
        driver.get(SPECIAL_URL);
        wait.waitSeconds(3);
    }

    // Del HTML: <span class="sale"></span> + <div class="pricenew"> + <div class="priceold">
    public boolean hasDiscountProducts() {
        List<WebElement> saleSpans = driver.findElements(By.cssSelector("span.sale"));
        List<WebElement> priceOld  = driver.findElements(By.cssSelector("div.priceold"));
        List<WebElement> priceNew  = driver.findElements(By.cssSelector("div.pricenew"));
        log.info("span.sale=" + saleSpans.size() +
                 " | priceold=" + priceOld.size() +
                 " | pricenew=" + priceNew.size());
        return !saleSpans.isEmpty() && !priceOld.isEmpty();
    }
}
