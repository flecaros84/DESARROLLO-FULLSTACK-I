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
pause
