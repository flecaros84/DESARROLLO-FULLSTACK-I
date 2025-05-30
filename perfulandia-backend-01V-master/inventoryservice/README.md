# Servicio de Inventario - Perfulandia

## 📋 Descripción General
El servicio de inventario es el encargado de gestionar el stock de productos en las diferentes sucursales de Perfulandia. Se comunica principalmente con el servicio de ventas para actualizar el inventario cuando se realizan transacciones.

## 🌐 Endpoints Principales

### 🔍 Obtener todo el inventario
```
GET /api/v1/inventario/todo
```
**Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "productoId": 1,
    "sucursalId": 1,
    "cantidad": 10
  }
]
```

### 🔍 Consultar stock específico
```
GET /api/v1/inventario/stock?productoId=1&sucursalId=1
```
**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "productoId": 1,
  "sucursalId": 1,
  "cantidad": 10
}
```

### ➕ Crear/Actualizar stock
```
POST /api/v1/inventario/stock
```
**Cuerpo de la petición:**
```json
{
  "productoId": 1,
  "sucursalId": 1,
  "cantidad": 15
}
```
**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "productoId": 1,
  "sucursalId": 1,
  "cantidad": 15
}
```

### 🔄 Ajustar stock (sumar/restar)
```
PATCH /api/v1/inventario/stock/ajustar
```
**Cuerpo de la petición:**
```json
{
  "productoId": 1,
  "sucursalId": 1,
  "cantidadAjuste": -2
}
```
**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "productoId": 1,
  "sucursalId": 1,
  "cantidad": 13
}
```

### ❌ Eliminar entrada de inventario
```
DELETE /api/v1/inventario/1
```
**Respuesta Exitosa (204 No Content)**

## 🤝 Comunicación con Otros Servicios

### 🔄 Servicio de Ventas
El servicio de inventario se integra directamente con el servicio de ventas a través de llamadas HTTP. Aquí está el flujo típico de interacción:

#### Flujo de una Venta
1. **Inicio de la Venta**: El servicio de ventas recibe una solicitud para crear una nueva venta con sus detalles.
2. **Verificación de Stock**: Para cada producto en la venta, el servicio de ventas llama al endpoint de ajuste de inventario:
   ```
   PATCH /api/v1/inventario/stock/ajustar
   {
     "productoId": 1,
     "sucursalId": 1,
     "cantidadAjuste": -2
   }
   ```
3. **Confirmación**: Si el ajuste es exitoso, la venta se procesa. Si falla, la venta se rechaza.

#### Detalle de Boleta
Cada venta genera un detalle de boleta que incluye:
- ID del producto
- Cantidad vendida
- Precio unitario
- Subtotal

#### Puntos Clave de la Integración
1. **Sincronización en Tiempo Real**: Los ajustes de inventario son síncronos con la venta, lo que garantiza consistencia inmediata.
2. **Manejo de Errores**: Si el servicio de inventario no está disponible, la venta no se puede completar.
3. **Consistencia de Datos**: No hay validación cruzada con el catálogo de productos, lo que podría llevar a inconsistencias.

#### Posibles Mejoras
1. **Patrón Saga**: Implementar un patrón Saga para manejar transacciones distribuidas.
2. **Event Sourcing**: Usar eventos para mantener consistencia eventual entre servicios.
3. **Circuit Breaker**: Implementar un patrón Circuit Breaker para manejar fallas en la comunicación entre servicios.

### ⚠️ Consideraciones de Integración
1. **Falta de Validación de Productos**: El inventario no valida si un producto existe en el catálogo antes de registrar su stock.
2. **Datos Huérfanos**: Si un producto se elimina, su inventario podría quedar huérfano.
3. **Transacciones Distribuidas**: No hay manejo de transacciones distribuidas, lo que podría causar inconsistencias.

## 🐛 Problemas Conocidos

1. **Actualización de stock**: El endpoint `POST /api/v1/inventario/stock` reemplaza el valor actual en lugar de sumar a la cantidad existente. Esto puede llevar a pérdida de información si no se maneja correctamente desde el cliente.

2. **Falta de validaciones**: 
   - No se valida que la cantidad sea un número positivo
   - No se verifica la existencia del producto en el catálogo
   - No hay manejo de stock negativo

3. **Documentación incompleta**: Faltan códigos de error HTTP específicos y mensajes de error descriptivos. Actualmente, el servicio no documenta claramente los posibles códigos de error que pueden devolverse en diferentes escenarios.

## 📝 Códigos de Estado HTTP

### Respuestas exitosas (2xx)
- `200 OK`: La solicitud se completó exitosamente y se devuelve el recurso solicitado.
  - Ejemplo: Consulta de stock, ajuste de inventario exitoso.
- `201 Created`: Recurso creado exitosamente.
  - Ejemplo: Creación de una nueva entrada de inventario.
- `204 No Content`: Operación exitosa sin contenido para devolver.
  - Ejemplo: Eliminación de una entrada de inventario.

### Errores del cliente (4xx)
- `400 Bad Request`: La solicitud es incorrecta o está mal formada.
  - Ejemplos: 
    - Faltan campos obligatorios
    - Tipos de datos incorrectos
    - Valores inválidos (ej. cantidades negativas en ciertos contextos)
  ```json
  {
    "timestamp": "2025-05-30T22:45:12.000+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "El campo 'cantidad' debe ser un número positivo",
    "path": "/api/v1/inventario/stock"
  }
  ```

- `404 Not Found`: El recurso solicitado no existe.
  - Ejemplos:
    - Producto no encontrado
    - Sucursal no existe
    - Entrada de inventario no encontrada
  ```json
  {
    "timestamp": "2025-05-30T22:45:12.000+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "No se encontró inventario para el producto 999 en la sucursal 1",
    "path": "/api/v1/inventario/stock"
  }
  ```

- `409 Conflict`: Conflicto con el estado actual del recurso.
  - Ejemplo: Intento de reducir el stock por debajo de cero
  ```json
  {
    "timestamp": "2025-05-30T22:45:12.000+00:00",
    "status": 409,
    "error": "Conflict",
    "message": "No hay suficiente stock disponible. Stock actual: 5, cantidad solicitada: 10",
    "path": "/api/v1/inventario/stock/ajustar"
  }
  ```

### Errores del servidor (5xx)
- `500 Internal Server Error`: Error interno del servidor.
  - Ejemplo: Error en la base de datos, excepción no controlada
  ```json
  {
    "timestamp": "2025-05-30T22:45:12.000+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Error al procesar la solicitud",
    "path": "/api/v1/inventario/stock"
  }
  ```

## 🔍 Mejoras Sugeridas para el Manejo de Errores

1. **Respuestas consistentes**: Todas las respuestas de error deberían seguir el mismo formato.
2. **Códigos HTTP específicos**: Usar códigos más específicos cuando sea apropiado (ej. `422 Unprocessable Entity` para errores de validación).
3. **Mensajes de error útiles**: Incluir mensajes que ayuden a solucionar el problema.
4. **Documentación Swagger/OpenAPI**: Documentar los códigos de respuesta en la especificación de la API.
5. **Códigos de error personalizados**: Para errores de negocio específicos, considerar el uso de códigos de error personalizados en la respuesta.

## 💡 Recomendaciones

1. **Integración con el servicio de productos**:
   - Validar la existencia de productos antes de actualizar el inventario
   - Sincronizar la creación/eliminación de productos con el inventario

2. **Mejoras en los endpoints**:
   - Agregar paginación en la consulta de todo el inventario
   - Incluir filtros por rango de fechas para consultas históricas
   - Agregar búsqueda por múltiples IDs de producto

3. **Seguridad**:
   - Implementar autenticación y autorización
   - Validar que los usuarios solo puedan acceder a inventario de sus sucursales autorizadas

4. **Monitoreo**:
   - Agregar métricas de uso
   - Implementar logs detallados para auditoría

## 🧪 Pruebas Realizadas

### Prueba 1: Crear nuevo registro de inventario
**Request:**
```
POST /api/v1/inventario/stock
{
  "productoId": 2,
  "sucursalId": 1,
  "cantidad": 20
}
```
**Resultado:** Registro creado exitosamente con cantidad = 20

### Prueba 2: Actualizar stock existente
**Request:**
```
POST /api/v1/inventario/stock
{
  "productoId": 2,
  "sucursalId": 1,
  "cantidad": 30
}
```
**Resultado:** Cantidad actualizada a 30 (no se sumó, se reemplazó)

### Prueba 3: Ajustar stock (sumar/restar)
**Request:**
```
PATCH /api/v1/inventario/stock/ajustar
{
  "productoId": 2,
  "sucursalId": 1,
  "cantidadAjuste": -5
}
```
**Resultado:** Cantidad actualizada a 25 (30 - 5)

### Prueba 4: Consultar stock específico
**Request:**
```
GET /api/v1/inventario/stock?productoId=2&sucursalId=1
```
**Resultado:** Se devolvió correctamente el registro con cantidad = 25
