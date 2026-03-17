Feature: Registro y Login en Automation Test Store

  Background:
    Given el usuario esta en Automation Test Store

  @registro @smoke
  Scenario: Registro de cuenta nueva con datos fijos
    When el usuario hace click en Register
    And llena el formulario de registro
    And envia el formulario
    Then debe ver el mensaje de cuenta creada

  @registro @random
  Scenario: Registro de cuenta con datos aleatorios
    When el usuario hace click en Register
    And llena el formulario con datos aleatorios generados por Faker
    And envia el formulario
    Then debe ver el mensaje de cuenta creada

  @registro @random
  Scenario: Registro y login con datos aleatorios en el mismo flujo
    When el usuario hace click en Register
    And llena el formulario con datos aleatorios generados por Faker
    And envia el formulario
    Then debe ver el mensaje de cuenta creada
    When hace login con las credenciales del usuario recien registrado
    Then debe estar logueado en la tienda

  @login @smoke
  Scenario: Login con usuario registrado
    When el usuario hace login con credenciales validas
    Then debe estar logueado en la tienda

  @login @negative
  Scenario: Login con credenciales invalidas
    When el usuario hace login con credenciales invalidas
    Then no debe estar logueado en la tienda
