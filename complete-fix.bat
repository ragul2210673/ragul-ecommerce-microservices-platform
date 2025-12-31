@echo off
echo ========================================
echo Complete Fix Script - MySQL Configuration
echo ========================================
echo.

echo [Step 1] Stopping all containers...
docker-compose down
echo.

echo [Step 2] Backing up old application.yml files...
if exist product-service\src\main\resources\application.yml (
    copy product-service\src\main\resources\application.yml product-service\src\main\resources\application.yml.backup
)
if exist user-service\src\main\resources\application.yml (
    copy user-service\src\main\resources\application.yml user-service\src\main\resources\application.yml.backup
)
if exist cart-service\src\main\resources\application.yml (
    copy cart-service\src\main\resources\application.yml cart-service\src\main\resources\application.yml.backup
)
if exist order-service\src\main\resources\application.yml (
    copy order-service\src\main\resources\application.yml order-service\src\main\resources\application.yml.backup
)
if exist payment-service\src\main\resources\application.yml (
    copy payment-service\src\main\resources\application.yml payment-service\src\main\resources\application.yml.backup
)
echo.

echo [Step 3] Copying corrected MySQL configuration files...
echo Copying Product Service config...
copy /Y product-application.yml product-service\src\main\resources\application.yml

echo Copying User Service config...
copy /Y user-application.yml user-service\src\main\resources\application.yml

echo Copying Cart Service config...
copy /Y cart-application.yml cart-service\src\main\resources\application.yml

echo Copying Order Service config...
copy /Y order-application.yml order-service\src\main\resources\application.yml

echo Copying Payment Service config...
copy /Y payment-application.yml payment-service\src\main\resources\application.yml
echo.

echo [Step 4] Rebuilding all services...
echo.

echo Building Product Service...
cd product-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Product Service build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

echo Building User Service...
cd user-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: User Service build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

echo Building Cart Service...
cd cart-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Cart Service build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

echo Building Order Service...
cd order-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Order Service build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

echo Building Payment Service...
cd payment-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Payment Service build failed!
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo [Step 5] Starting all services with Docker Compose...
docker-compose up -d --build

echo.
echo [Step 6] Waiting 60 seconds for services to start...
timeout /t 60

echo.
echo ========================================
echo Checking Service Status
echo ========================================
docker-compose ps

echo.
echo ========================================
echo Testing Services
echo ========================================

echo Testing Product Service...
curl http://localhost:8081/api/products
echo.

echo Testing User Service...
curl http://localhost:8082/api/users
echo.

echo Testing Cart Service...
curl http://localhost:8083/api/cart/1
echo.

echo Testing Order Service...
curl http://localhost:8084/api/orders
echo.

echo Testing Payment Service...
curl http://localhost:8085/api/payments
echo.

echo ========================================
echo Fix Complete!
echo ========================================
echo.
echo If you see "200 OK" or "[]" responses above, SUCCESS!
echo If still failing, run: docker-compose logs [service-name]
echo.
pause
