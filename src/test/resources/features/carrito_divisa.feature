Feature: Carrito, Divisa y Descuentos en Automation Test Store

  Background:
    Given el usuario esta en Automation Test Store
    And el usuario esta autenticado en la tienda

  @carrito @smoke
  Scenario: Agregar producto al carrito y verificar en CART
    When agrega el primer producto al carrito de la tienda
    And navega al carrito via header
    Then el carrito de la tienda debe tener al menos 1 producto

  @carrito @regression
  Scenario: Editar carrito eliminando un item con icono de basura
    When agrega el primer producto al carrito de la tienda
    And navega al carrito via header
    Then el carrito de la tienda debe tener al menos 1 producto
    And elimina el primer producto del carrito con el icono de basura
    Then el carrito debe tener menos productos

  @divisa @regression
  Scenario: Cambiar divisa de USD a EUR
    When agrega el primer producto al carrito de la tienda
    And navega al carrito via header
    And cambia la divisa a EUR usando la URL directa
    Then la divisa debe mostrar EURO en el encabezado

  @descuento @regression
  Scenario: Validar productos con descuento Sale en pagina especiales
    When navega a la pagina de ofertas especiales
    Then debe haber productos con etiqueta Sale o precio de descuento
