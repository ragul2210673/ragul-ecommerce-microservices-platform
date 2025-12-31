#!/bin/bash

echo "🔍 E-commerce Microservices Health Check"
echo "========================================"

# Check container status
echo ""
echo "📦 Container Status:"
docker-compose ps

echo ""
echo "🏥 Health Endpoint Tests:"

# Test User Service
echo -n "User Service (8081): "
if curl -s http://localhost:8081/actuator/health | grep -q "UP"; then
    echo "✅ HEALTHY"
else
    echo "❌ UNHEALTHY"
fi

# Test Product Service  
echo -n "Product Service (8082): "
if curl -s http://localhost:8082/actuator/health | grep -q "UP"; then
    echo "✅ HEALTHY"
else
    echo "❌ UNHEALTHY"
fi

# Test Cart Service
echo -n "Cart Service (8083): "
if curl -s http://localhost:8083/actuator/health | grep -q "UP"; then
    echo "✅ HEALTHY"
else
    echo "❌ UNHEALTHY"
fi

echo ""
echo "🗄️ Database Tests:"

# Test MySQL
echo -n "MySQL Connection: "
if docker exec ecommerce-mysql mysql -u ecommerce_user -pecommerce_password ecommerce_db -e "SELECT 1;" &>/dev/null; then
    echo "✅ CONNECTED"
else
    echo "❌ CONNECTION FAILED"
fi

# Test Redis
echo -n "Redis Connection: "
if docker exec ecommerce-redis redis-cli ping | grep -q "PONG"; then
    echo "✅ CONNECTED"
else
    echo "❌ CONNECTION FAILED"
fi

echo ""
echo "🔗 Service URLs:"
echo "User Service:    http://localhost:8081/actuator/health"
echo "Product Service: http://localhost:8082/actuator/health"
echo "Cart Service:    http://localhost:8083/actuator/health"

echo ""
echo "📊 Live Logs Command:"
echo "docker-compose logs -f"