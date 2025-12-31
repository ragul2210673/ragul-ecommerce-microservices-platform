-- View: Product inventory status
CREATE OR REPLACE VIEW V_PRODUCT_INVENTORY AS
SELECT 
    name as product_name,
    category,
    stock_quantity,
    price,
    (stock_quantity * price) as inventory_value,
    CASE 
        WHEN stock_quantity = 0 THEN 'OUT_OF_STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW_STOCK'
        WHEN stock_quantity <= 50 THEN 'MEDIUM_STOCK'
        ELSE 'WELL_STOCKED'
    END as stock_status,
    created_at,
    updated_at
FROM PRODUCTS
ORDER BY stock_quantity ASC;

-- View: Products by category
CREATE OR REPLACE VIEW V_PRODUCTS_BY_CATEGORY AS
SELECT 
    category,
    COUNT(*) as product_count,
    AVG(price) as avg_price,
    MIN(price) as min_price,
    MAX(price) as max_price,
    SUM(stock_quantity) as total_stock
FROM PRODUCTS
GROUP BY category
ORDER BY product_count DESC;

-- View: Active products summary
CREATE OR REPLACE VIEW V_ACTIVE_PRODUCTS_SUMMARY AS
SELECT 
    COUNT(*) as total_active_products,
    AVG(price) as average_price,
    SUM(stock_quantity) as total_inventory,
    MIN(created_at) as oldest_product_date,
    MAX(created_at) as newest_product_date
FROM PRODUCTS
WHERE is_active = 1;