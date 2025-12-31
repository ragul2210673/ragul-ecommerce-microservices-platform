@echo off
echo ========================================
echo Docker Services Diagnostic Tool
echo ========================================
echo.

echo [1] Checking Container Status...
echo ----------------------------------------
docker-compose ps
echo.

echo [2] Checking Service Logs (Last 50 lines each)...
echo ----------------------------------------

echo.
echo === Product Service Logs ===
docker-compose logs --tail=50 product-service
echo.

echo === User Service Logs ===
docker-compose logs --tail=50 user-service
echo.

echo === Cart Service Logs ===
docker-compose logs --tail=50 cart-service
echo.

echo === Order Service Logs ===
docker-compose logs --tail=50 order-service
echo.

echo === Payment Service Logs ===
docker-compose logs --tail=50 payment-service
echo.

echo [3] Testing Database Connections...
echo ----------------------------------------
echo Testing Product DB...
docker exec -it product-db mysqladmin ping -h localhost -u root -proot123

echo Testing User DB...
docker exec -it user-db mysqladmin ping -h localhost -u root -proot123

echo Testing Cart DB...
docker exec -it cart-db mysqladmin ping -h localhost -u root -proot123

echo Testing Order DB...
docker exec -it order-db mysqladmin ping -h localhost -u root -proot123

echo Testing Payment DB...
docker exec -it payment-db mysqladmin ping -h localhost -u root -proot123

echo.
echo [4] Testing API Endpoints...
echo ----------------------------------------
echo Testing Product Service (8081)...
curl -s http://localhost:8081/api/products
echo.

echo Testing User Service (8082)...
curl -s http://localhost:8082/api/users
echo.

echo Testing Cart Service (8083)...
curl -s http://localhost:8083/api/cart/1
echo.

echo Testing Order Service (8084)...
curl -s http://localhost:8084/api/orders
echo.

echo Testing Payment Service (8085)...
curl -s http://localhost:8085/api/payments
echo.

echo [5] Network Information...
echo ----------------------------------------
docker network inspect spring-ecommerce_ecommerce-network

echo.
echo ========================================
echo Diagnostic Complete
echo ========================================
pause
