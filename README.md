[README-caso2-automationstore.md](https://github.com/user-attachments/files/26058175/README-caso2-automationstore.md)
[Uploading README-caso2-automatio# Caso 2 - Automation Test Store Automation

## Descripción

Proyecto de automatización de pruebas UI para [Automation Test Store](http://www.automationteststore.com),

Cubre los flujos de una tienda e-commerce real: registro de cuentas con validación completa de formulario, autenticación, gestión del carrito de compras, cambio de divisas y validación de productos en oferta. La arquitectura sigue el patrón Page Object Model con Page Factory y BDD con Cucumber y Gherkin.

---

## Instalación

**Prerequisitos**

- Java 21
- Maven 3.8 o superior
- Google Chrome
- Git

**Pasos**

```bash
git clone https://github.com/tu-usuario/caso2-automationstore.git
cd caso2-automationstore
mvn clean install -DskipTests
```

ChromeDriver es gestionado automáticamente por Selenium Manager. No se requiere instalación manual.

---

## Uso

Ejecutar la suite completa:

```bash
mvn clean test
```

Ejecutar por etiquetas:

```bash
  mvn clean test "-Dcucumber.filter.tags=@smoke"
mvn clean test "-Dcucumber.filter.tags=@regression"
mvn clean test "-Dcucumber.filter.tags=@registro"
mvn clean test "-Dcucumber.filter.tags=@carrito"
mvn clean test "-Dcucumber.filter.tags=@divisa"
mvn clean test "-Dcucumber.filter.tags=@descuento"
```

Desde Eclipse: click derecho en `testng.xml` → Run As → TestNG Suite.

Los reportes se generan en la carpeta `reports/` al terminar la ejecución.

---

## Configuración

Archivo: `src/test/resources/config.properties`

```properties
url=http://www.automationteststore.com
browser=chrome
implicit.wait=10
explicit.wait=15
page.load.timeout=30

valid.user=johnqa_envioclick
valid.pass=Test1234!
invalid.user=usuariofalso999
invalid.pass=wrongpass

reg.firstname=John
reg.lastname=QA
reg.email=johnqa_envioclick@mailtest.com
reg.address=Calle Reforma 100
reg.city=Ciudad de Mexico
reg.postcode=06600
reg.loginname=johnqa_envioclick
reg.password=Test1234!

currency.usd=USD
currency.eur=EUR
currency.gbp=GBP
```

El usuario `johnqa_envioclick` debe estar registrado en el sitio antes de ejecutar los tests de login. Los escenarios de registro generan un loginname único por ejecución para evitar conflictos con usuarios duplicados.

---

## Estructura del Proyecto

```
src/test/java/
  hooks/        - ciclo de vida de los tests
  pages/        - page objects
  stepdefinitions/ - steps de cucumber
  runners/      - configuracion del runner
  utils/        - utilidades comunes

src/test/resources/
  features/     - escenarios gherkin
  config.properties
  testng.xml
```

---

## Estructura del Framework

**Hooks**
Se ejecuta antes y después de cada escenario. Abre el navegador, navega a la URL del sitio y cierra el driver al finalizar. En caso de fallo captura un screenshot y lo adjunta al reporte de Cucumber.

**BasePage**
Clase base para todos los Page Objects. Inicializa Page Factory, WaitHelper y ActionsHelper en el constructor. Todas las páginas heredan de esta clase.

**RegisterPage**
Maneja el formulario completo de creación de cuenta. Navega directamente a la URL de registro, llena todos los campos obligatorios incluyendo teléfono, país, y el checkbox de Privacy Policy. Selecciona el radio button de newsletter y hace submit con scroll previo al botón para garantizar visibilidad.

**StorePage**
Page Object principal del sitio. Concentra el login por URL directa, la adición de productos al carrito mediante click en el botón AJAX con rotación de producto por escenario, la navegación al carrito, la eliminación de items por URL directa del botón de basura, el cambio de divisa por URL directa y la validación de productos en la página de ofertas especiales.

**StoreSteps**
Conecta todos los pasos Gherkin del sitio con los métodos de los Page Objects. Mantiene el contador de items del carrito entre steps para validar correctamente los escenarios de edición.

**TestRunner**
Configura `@CucumberOptions` con features, glue y plugins. Integra ExtentReports con el adapter oficial de Cucumber 7.

**DriverFactory**
Gestiona el ciclo de vida del WebDriver con `ThreadLocal`. El timeout de página está configurado a 60 segundos para adaptarse a la velocidad de respuesta del sitio.

**ConfigManager**
Singleton que lee `config.properties`. Expone getters tipados para cada configuración del ambiente.

**WaitHelper**
Abstracción sobre `WebDriverWait`. Provee `waitForVisible`, `waitForClickable`, `waitForPresence` y `waitForAlert`.

**ActionsHelper**
Scroll, hover, click via JavaScript y cambio de tabs. Se usa en este proyecto principalmente para hacer click en botones que requieren scroll previo.

**RandomDataHelper**
Genera datos únicos con JavaFaker más timestamp. Se usa en el registro para evitar el error de loginname duplicado entre ejecuciones.

---

## Casos de Prueba

**registro_login.feature**

| Escenario | Tag |
|---|---|
| Registro con datos base y loginname único | @registro @smoke |
| Registro con datos completamente aleatorios | @registro @random |
| Registro y login con el mismo usuario generado | @registro @random |
| Login con usuario registrado | @login @smoke |
| Login con credenciales inválidas | @login @negative |

**carrito_divisa.feature**

| Escenario | Tag |
|---|---|
| Agregar producto y verificar cantidad en carrito | @carrito @smoke |
| Eliminar item con icono de basura y validar carrito | @carrito @regression |
| Cambiar divisa de USD a EUR | @divisa @regression |
| Validar productos con etiqueta Sale en Specials | @descuento @regression |

---

## Tecnologías Usadas

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Selenium WebDriver | 4.20.0 |
| Cucumber | 7.15.0 |
| TestNG | 7.10.2 |
| ExtentReports | 5.1.1 |
| JavaFaker | 1.0.2 |
| Log4j | 2.23.1 |
| Maven | 3.8+ |

---

## Sitio Bajo Prueba

El sitio es una tienda construida sobre AbanteCart, diseñada para práctica de automatización. El carrito usa AJAX para agregar productos sin recargar la página. El cambio de divisa se realiza por URL directa. El formulario de registro requiere todos los campos obligatorios incluyendo aceptación explícita de política de privacidad.

URL: http://www.automationteststore.com

---

## Contribución

```bash
git checkout -b feature/nombre-del-cambio
git commit -m "descripcion del cambio"
git push origin feature/nombre-del-cambio
```

Abrir Pull Request describiendo qué se automatizó y por qué.



Proyecto desarrollado como evaluación técnica de QA para Envíoclick. Uso educativo.
nstore.md…]()
