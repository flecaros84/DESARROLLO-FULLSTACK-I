# Servicio de Inventario (inventoryservice)

## Descripción General

El **Servicio de Inventario (inventoryservice)** es un microservicio responsable de gestionar las existencias o el stock de los productos. Proporciona funcionalidades para consultar y actualizar la cantidad de productos disponibles.

Este servicio también interactúa con otros microservicios, como el `productservice`, para obtener información detallada de los productos cuando es necesario.

## Tecnologías Utilizadas

*   Java 21
*   Spring Boot
*   Spring MVC (para exponer APIs REST)
*   Spring Data JPA (para la persistencia de datos de inventario, si aplica localmente)
*   Spring WebFlux (`WebClient` para comunicación HTTP reactiva con otros servicios)
*   Lombok
*   Maven (o Gradle, según tu configuración)

## Funcionalidades Principales

*   **Gestión de Inventario**: Permite consultar y, potencialmente, crear o actualizar registros de inventario. Cada registro de inventario suele estar asociado a un ID de producto y una cantidad.
*   **Consulta Enriquecida**: Ofrece endpoints que combinan la información de stock local con detalles del producto obtenidos de `productservice`.

## Modelos de Datos Clave

### `Inventario`
Representa la entidad principal de este servicio. Contiene información sobre la cantidad disponible de un producto específico.
*   `id` (Long): Identificador único del registro de inventario.
*   `productoId` (Long): Identificador del producto al que se refiere este registro de inventario (este ID se usa para consultar `productservice`).
*   `cantidad` (Integer/Long): Número de unidades disponibles en stock.
    *(Otros campos según sean necesarios)*

### `Producto` (DTO)
Es un Objeto de Transferencia de Datos (DTO) utilizado para mapear la respuesta obtenida del `productservice` al consultar detalles de un producto.
*   `id` (Long): Identificador único del producto.
*   `nombre` (String): Nombre del producto.
*   `precio` (double): Precio del producto.
    *(Otros campos según la estructura de datos de `productservice`)*

## APIs Expuestas

El servicio expone los siguientes endpoints REST bajo el path base `/api/v1/inventario`:

*   **`GET /api/v1/inventario/{id}`**: Obtiene los detalles de un registro de inventario específico por su ID.
*   **`POST /api/v1/inventario`**: Crea un nuevo registro de inventario.
    *   **Body**: Objeto JSON `Inventario`.
*   **`GET /api/v1/inventario/{inventarioId}/producto/{productoId}`**: Obtiene los detalles de un registro de inventario y los enriquece con la información del producto (nombre, precio, etc.) obtenida del `productservice`.
    *   Este endpoint demuestra la interacción entre servicios.

*(Pueden existir otros endpoints para actualizar, eliminar, o listar inventarios).*

## Interacción con Otros Servicios

### `productservice`

El `inventoryservice` necesita obtener información detallada sobre los productos (como nombre y precio) que no almacena localmente. Para ello, realiza llamadas HTTP al `productservice`.

*   **Mecanismo**: Se utiliza `WebClient` de Spring WebFlux para realizar peticiones HTTP GET no bloqueantes.
*   **Endpoint de `productservice` Invocado (Ejemplo)**: `GET {productservice.baseurl}/{id}` (donde `{productservice.baseurl}` es la URL base del servicio de productos, por ejemplo, `http://localhost:8082/api/v1/productos`, y `{id}` es el ID del producto).
*   **Flujo**:
    1.  Una solicitud llega a un endpoint de `inventoryservice` que requiere detalles del producto (ej. `GET /api/v1/inventario/{inventarioId}/producto/{productoId}`).
    2.  El `InventarioService` (o un componente similar) utiliza el `WebClient` configurado.
    3.  Se construye una petición GET a `productservice` utilizando el `productoId`.
    4.  `inventoryservice` espera la respuesta de `productservice`.
    5.  La respuesta JSON de `productservice` se deserializa al DTO `Producto`.
    6.  La información del `Producto` se combina con la información del `Inventario` para construir la respuesta final.
*   **Configuración Requerida**: La URL base del `productservice` debe estar configurada en `inventoryservice`, típicamente en `application.properties` o `application.yml`, y ser leída para la creación del bean `WebClient`. Por ejemplo:
    ```properties
    # application.properties
    productservice.baseurl=http://localhost:8082/api/v1/productos
    ```
    Esta URL se usa en la configuración del bean `WebClient` (ej. en una clase `AppConfig.java`).

## Configuración del Servicio

Las configuraciones principales se encuentran en `src/main/resources/application.properties` (o `application.yml`).

*   **Puerto del servidor**: `server.port` (ej. `server.port=8081`)
*   **Configuración de la base de datos (si aplica)**: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, etc.
*   **URL del `productservice`**: `productservice.baseurl` (como se mencionó anteriormente).

## Cómo Construir y Ejecutar

### Requisitos Previos

*   JDK 21 o superior.
*   Maven 3.6+ o Gradle 7+ (según el proyecto).
*   El `productservice` (u otros servicios dependientes) debe estar accesible si se prueban flujos que interactúan con él.

### Construcción

Navega al directorio raíz del módulo `inventoryservice` y ejecuta:

*   **Con Maven**:
    ```bash
    mvn clean install
    ```
*   **Con Gradle**:
    ```bash
    ./gradlew clean build
    ```

### Ejecución

Una vez construido, puedes ejecutar el servicio:

*   **Con Maven (después de `install`)**:
    ```bash
    java -jar target/inventoryservice-0.0.1-SNAPSHOT.jar
    ```
    (El nombre del archivo `.jar` puede variar).
*   **Con Gradle**:
    ```bash
    ./gradlew bootRun
    ```
O puedes ejecutarlo desde tu IDE (IntelliJ IDEA, Eclipse, etc.).

## Cómo Probar

Puedes probar los endpoints utilizando herramientas como `curl`, Postman, o cualquier cliente HTTP.

**Ejemplo: Obtener inventario enriquecido con detalles del producto**

Asegúrate de que tanto `inventoryservice` como `productservice` estén corriendo.

*   Supón que `inventoryservice` corre en el puerto `8081`.
*   Supón que existe un registro de inventario con `id=1` y `productoId=101`.
*   Supón que `productservice` tiene un producto con `id=101`.

Petición `GET`: