# 📦 SalesService

Microservicio de gestión de ventas para la plataforma **Perfulandia**. Este servicio se encarga de manejar las operaciones relacionadas con las ventas, incluyendo la creación, actualización y consulta de transacciones de venta.

## 🚀 Características principales

- **Gestión de ventas**: Permite crear, actualizar y consultar registros de ventas.
- **Integración con microservicios**: Consulta datos de productos y usuarios mediante peticiones REST.
- **API RESTful**: Exposición de endpoints para operaciones CRUD sobre las ventas.
- **Persistencia de datos**: Utiliza una base de datos relacional para almacenar la información de las ventas.

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Java
- **Framework**: Spring Boot
- **Base de datos**: MySQL
- **Cliente HTTP**: `RestTemplate` para comunicarse con otros microservicios
- **Herramientas de construcción**: Maven
- **Control de versiones**: Git

## 📂 Estructura del proyecto

```
saleservice/
├── controller/
├── model/
├── repository/
├── service/
└── SaleserviceApplication.java
```

## ⚙️ Configuración y ejecución

### Prerrequisitos

- Java 11 o superior
- Maven 3.6 o superior
- MySQL 5.7 o superior

### Pasos para ejecutar el servicio

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/flecaros84/DESARROLLO-FULLSTACK-I.git
   cd DESARROLLO-FULLSTACK-I/perfulandia-backend-01V-master/saleservice
   ```

2. **Configurar la base de datos**:

   Crear una base de datos en MySQL y actualizar el archivo `application.properties` con las credenciales correspondientes.

3. **Construir el proyecto**:

   ```bash
   mvn clean install
   ```

4. **Ejecutar la aplicación**:

   ```bash
   mvn spring-boot:run
   ```

   La aplicación estará disponible en `http://localhost:8080`.

## 📬 Endpoints principales

- `GET /ventas`: Lista todas las ventas.
- `GET /ventas/{id}`: Obtiene los detalles de una venta por su ID.
- `POST /ventas`: Registra una nueva venta (incluyendo detalles y factura).
- `GET /ventas/producto/{id}`: Consulta información de un producto desde el microservicio de productos.
- `GET /ventas/usuario/{id}`: Consulta información de un usuario desde el microservicio de usuarios.

### 📝 Ejemplo de solicitud `POST /ventas`

```json
{
  "clienteId": 45678,
  "medioPago": "DEBITO",
  "detalles": [
    {
      "productoId": 110,
      "cantidad": 2,
      "precioUnitario": 4990.0
    },
    {
      "productoId": 220,
      "cantidad": 1,
      "precioUnitario": 8990.0
    }
  ]
}
```

#### 🔎 Descripción de los campos:

- `clienteId`: ID del usuario (cliente) que realiza la compra.
- `medioPago`: Método de pago utilizado (por ejemplo: `"EFECTIVO"`, `"DEBITO"`, `"CREDITO"`).
- `detalles`: Lista de productos incluidos en la venta:
  - `productoId`: ID del producto vendido.
  - `cantidad`: Cantidad comprada.
  - `precioUnitario`: Precio unitario del producto al momento de la venta.

> 📝 Nota: El campo `fecha` es gestionado automáticamente por el backend y no debe incluirse en la solicitud.

## 🧱 Estructura de Clases

### 📌 1. Modelos (`model`)

- **`Venta.java`**  
  Representa una venta realizada. Incluye atributos como ID, cliente, total, medio de pago y una lista de detalles (`DetalleVenta`). Está relacionada con `Factura` y `Usuario`.

- **`DetalleVenta.java`**  
  Define los productos individuales dentro de una venta. Incluye cantidad, precio unitario, y una relación con `Venta`.

- **`Factura.java`**  
  Contiene los datos de facturación asociados a una venta, como número de factura, fecha de emisión y monto total.

- **`Producto.java`**  
  Clase auxiliar usada para consultar datos de productos a través de un servicio REST externo. No es persistida en la base de datos local.

- **`Usuario.java`**  
  Clase auxiliar para representar datos de clientes consultados mediante REST al microservicio de usuarios.

### 📌 2. Repositorios (`repository`)

- **`VentaRepository.java`**  
  Extiende `JpaRepository`. Permite operaciones CRUD sobre ventas.

- **`DetalleVentaRepository.java`**  
  Interface para acceder a los detalles de cada venta.

- **`FacturaRepository.java`**  
  Permite persistencia y consulta de facturas emitidas.

### 📌 3. Servicios (`service`)

- **`VentaService.java`**  
  Contiene la lógica de negocio para registrar ventas, calcular totales, y coordinar la creación de detalles y facturas. También gestiona las consultas a los microservicios de productos y usuarios mediante `RestTemplate`.

- **`FacturaService.java`**  
  Maneja la creación y persistencia de facturas, trabajando junto con `VentaService`.

### 📌 4. Controladores (`controller`)

- **`VentaController.java`**  
  Expone los endpoints REST para manejar operaciones relacionadas con las ventas:  
  - Registrar nueva venta.
  - Listar ventas.
  - Consultar venta por ID.
  - Consultar producto por ID mediante REST (`/ventas/producto/{id}`).
  - Consultar usuario por ID mediante REST (`/ventas/usuario/{id}`).

## 🧪 Pruebas

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, sigue los siguientes pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y haz commit: `git commit -m 'Agrega nueva funcionalidad'`.
4. Sube tus cambios: `git push origin feature/nueva-funcionalidad`.
5. Abre un Pull Request.

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](../LICENSE) para más detalles.
