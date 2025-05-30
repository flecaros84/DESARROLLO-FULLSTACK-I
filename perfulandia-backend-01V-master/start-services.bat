@echo off
echo Deteniendo servicios Java existentes...
taskkill /F /IM java.exe >nul 2>&1

start "Usuarios (8081)" cmd /k "cd /d %~dp0usuarioservice && mvn spring-boot:run"
timeout /t 10 >nul

start "Productos (8082)" cmd /k "cd /d %~dp0productservice && mvn spring-boot:run"
timeout /t 10 >nul

start "Inventario (8083)" cmd /k "cd /d %~dp0inventoryservice && mvn spring-boot:run"
timeout /t 10 >nul

start "Ventas (8084)" cmd /k "cd /d %~dp0saleservice && mvn spring-boot:run"

echo.
echo Todos los servicios se están iniciando...
echo.
echo URLs de los servicios:
echo - Usuarios:    http://localhost:8081/api/usuarios
echo - Productos:  http://localhost:8082/api/productos
echo - Inventario: http://localhost:8083/api/v1/inventario/todo
echo - Ventas:     http://localhost:8084/api/ventas
echo.
echo Los servicios se están iniciando en ventanas de consola separadas.
echo Espere unos segundos hasta que todos los servicios estén listos.

timeout /t 20 >nul

echo.
echo Probando conexión con los servicios...

echo Probando servicio de Usuarios (8081):
curl -s -o nul -w "%%{http_code}" http://localhost:8081/api/usuarios

echo.
echo Probando servicio de Productos (8082):
curl -s -o nul -w "%%{http_code}" http://localhost:8082/api/productos

echo.
echo Probando servicio de Inventario (8083):
curl -s -o nul -w "%%{http_code}" http://localhost:8083/api/v1/inventario/todo

echo.
echo Probando servicio de Ventas (8084):
curl -s -o nul -w "%%{http_code}" http://localhost:8084/api/ventas

echo.
echo Si ve c�digos 200 o 201, los servicios están funcionando correctamente.
echo Si ve c�digos de error (4xx o 5xx), revise las ventanas de consola de los servicios.

pause
