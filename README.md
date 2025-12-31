# E-Commerce Platform with Oracle Database

Production-ready e-commerce microservices platform built with Spring Boot and Oracle Database 23c.

## Architecture

- **Product Service** (8081): Product catalog, inventory management
- **User Service** (8082): User authentication, profile management  
- **Cart Service** (8083): Shopping cart operations
- **Oracle Database** (1521): Primary data store with PL/SQL procedures, triggers, and views
- **Redis** (6379): Caching layer
- **Prometheus** (9090): Metrics monitoring
- **Grafana** (3000): Visualization dashboards

## Oracle-Specific Features

### Database Objects
- **Stored Procedures**: `calculate_cart_total` - Computes order totals with discounts
- **Triggers**: 
  - `trg_log_inventory_change` - Audit trail for stock changes
  - `trg_check_stock_level` - Prevents negative inventory
  - `trg_update_product_timestamp` - Auto-updates timestamps
- **Views**:
  - `V_PRODUCT_INVENTORY` - Real-time inventory status
  - `V_PRODUCTS_BY_CATEGORY` - Category analytics
  - `V_ACTIVE_PRODUCTS_SUMMARY` - Product statistics

### Technologies
- Spring Boot 3.2
- Oracle Database 23c Free
- Oracle JDBC 23.3.0
- Hibernate with Oracle12cDialect
- HikariCP connection pooling

## Quick Start

### Prerequisites
- Docker Desktop
- Java 17+
- Maven 3.6+

### Run with Docker Compose
```bash
# Build and start all services
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f product-service